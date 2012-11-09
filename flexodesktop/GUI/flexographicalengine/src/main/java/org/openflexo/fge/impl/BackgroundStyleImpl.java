/*
 * (c) Copyright 2010-2011 AgileBirds
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openflexo.fge.impl;

import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.fge.BackgroundStyle;
import org.openflexo.fge.GraphicalRepresentation;
import org.openflexo.fge.notifications.FGENotification;

import sun.awt.image.ImageRepresentation;
import sun.awt.image.ToolkitImage;

public abstract class BackgroundStyleImpl extends FGEStyleImpl implements BackgroundStyle {
	private static final Logger logger = Logger.getLogger(BackgroundStyleImpl.class.getPackage().getName());

	private boolean useTransparency = false;
	private float transparencyLevel = 0.5f; // Between 0.0 and 1.0

	@Override
	public abstract Paint getPaint(GraphicalRepresentation<?> gr, double scale);

	@Override
	public abstract BackgroundStyleType getBackgroundStyleType();

	@Override
	public abstract String toString();

	public static class NoneImpl extends BackgroundStyleImpl implements None {
		@Override
		public Paint getPaint(GraphicalRepresentation<?> gr, double scale) {
			return null;
		}

		@Override
		public BackgroundStyleType getBackgroundStyleType() {
			return BackgroundStyleType.NONE;
		}

		@Override
		public String toString() {
			return "BackgroundStyle.NONE";
		}
	}

	public static class ColorImpl extends BackgroundStyleImpl implements Color {
		private java.awt.Color color = java.awt.Color.WHITE;

		@Override
		public Paint getPaint(GraphicalRepresentation<?> gr, double scale) {
			return color;
		}

		@Override
		public java.awt.Color getColor() {
			return color;
		}

		@Override
		public BackgroundStyleType getBackgroundStyleType() {
			return BackgroundStyleType.COLOR;
		}

		@Override
		public void setColor(java.awt.Color aColor) {
			if (requireChange(this.color, aColor)) {
				java.awt.Color oldColor = color;
				this.color = aColor;
				setChanged();
				notifyObservers(new FGENotification(Parameters.color, oldColor, aColor));
			}
		}

		@Override
		public String toString() {
			return "BackgroundStyle.COLOR(" + getColor() + ")";
		}

		private boolean requireChange(Object oldObject, Object newObject) {
			if (oldObject == null) {
				if (newObject == null) {
					return false;
				} else {
					return true;
				}
			}
			return !oldObject.equals(newObject);
		}

	}

	public static class ColorGradientImpl extends BackgroundStyleImpl implements ColorGradient {
		private java.awt.Color color1 = java.awt.Color.WHITE;
		private java.awt.Color color2 = java.awt.Color.BLACK;
		private ColorGradientDirection direction = ColorGradientDirection.SOUTH_EAST_NORTH_WEST;

		@Override
		public Paint getPaint(GraphicalRepresentation<?> gr, double scale) {
			switch (direction) {
			case SOUTH_EAST_NORTH_WEST:
				return new GradientPaint(0, 0, color1, gr.getViewWidth(scale), gr.getViewHeight(scale), color2);
			case SOUTH_WEST_NORTH_EAST:
				return new GradientPaint(0, gr.getViewHeight(scale), color1, gr.getViewWidth(scale), 0, color2);
			case WEST_EAST:
				return new GradientPaint(0, 0.5f * gr.getViewHeight(scale), color1, gr.getViewWidth(scale), 0.5f * gr.getViewHeight(scale),
						color2);
			case NORTH_SOUTH:
				return new GradientPaint(0.5f * gr.getViewWidth(scale), 0, color1, 0.5f * gr.getViewWidth(scale), gr.getViewHeight(scale),
						color2);
			default:
				return new GradientPaint(0, 0, color1, gr.getViewWidth(scale), gr.getViewHeight(scale), color2);
			}
		}

		@Override
		public BackgroundStyleType getBackgroundStyleType() {
			return BackgroundStyleType.COLOR_GRADIENT;
		}

		@Override
		public java.awt.Color getColor1() {
			return color1;
		}

		@Override
		public void setColor1(java.awt.Color aColor) {
			if (requireChange(this.color1, aColor)) {
				java.awt.Color oldColor = color1;
				this.color1 = aColor;
				setChanged();
				notifyObservers(new FGENotification(Parameters.color1, oldColor, aColor));
			}
		}

		@Override
		public java.awt.Color getColor2() {
			return color2;
		}

		@Override
		public void setColor2(java.awt.Color aColor) {
			if (requireChange(this.color2, aColor)) {
				java.awt.Color oldColor = color2;
				this.color2 = aColor;
				setChanged();
				notifyObservers(new FGENotification(Parameters.color2, oldColor, aColor));
			}
		}

		@Override
		public ColorGradientDirection getDirection() {
			return direction;
		}

		@Override
		public void setDirection(ColorGradientDirection aDirection) {
			if (requireChange(this.direction, aDirection)) {
				ColorGradientDirection oldTexture = direction;
				this.direction = aDirection;
				setChanged();
				notifyObservers(new FGENotification(Parameters.direction, oldTexture, aDirection));
			}
		}

		@Override
		public String toString() {
			return "BackgroundStyle.COLOR_GRADIENT(" + getColor1() + "," + getColor2() + "," + getDirection() + ")";
		}

		private boolean requireChange(Object oldObject, Object newObject) {
			if (oldObject == null) {
				if (newObject == null) {
					return false;
				} else {
					return true;
				}
			}
			return !oldObject.equals(newObject);
		}

	}

	public static class TextureImpl extends BackgroundStyleImpl implements Texture {
		private TextureType textureType = TextureType.TEXTURE1;
		private java.awt.Color color1 = java.awt.Color.WHITE;
		private java.awt.Color color2 = java.awt.Color.BLACK;
		private BufferedImage coloredTexture;
		private ToolkitImage coloredImage;

		/**
		 * This constructor should not be used, as it is invoked by PAMELA framework to create objects, as well as during deserialization
		 */
		public TextureImpl() {
			super();
			rebuildColoredTexture();
		}

		private void rebuildColoredTexture() {
			if (textureType == null) {
				return;
			}
			final Image initialImage = textureType.getImageIcon().getImage();
			ColorSwapFilter imgfilter = new ColorSwapFilter(java.awt.Color.BLACK, color1, java.awt.Color.WHITE, color2) {
				@Override
				public void imageComplete(int status) {
					super.imageComplete(status);
					coloredTexture = new BufferedImage(initialImage.getWidth(null), initialImage.getHeight(null),
							BufferedImage.TYPE_INT_ARGB);
					Graphics gi = coloredTexture.getGraphics();
					gi.drawImage(coloredImage, 0, 0, null);
				}
			};

			ImageProducer producer = new FilteredImageSource(initialImage.getSource(), imgfilter);
			coloredImage = (ToolkitImage) Toolkit.getDefaultToolkit().createImage(producer);
			ImageRepresentation consumer = new ImageRepresentation(coloredImage, null, true);
			producer.addConsumer(consumer);
			try {
				producer.startProduction(consumer);
			} catch (RuntimeException e) {
				logger.warning("Unexpected exception: " + e);
			}

		}

		private BufferedImage getColoredTexture() {
			if (coloredTexture == null) {
				rebuildColoredTexture();
				/*int tests = 10; // Time-out = 1s
				while (coloredTexture == null && tests>=0) {
					try {
						tests--;
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (coloredTexture == null) {
					logger.warning("Could not compute colored texture");
					Image initialImage = textureType.getImageIcon().getImage();
					System.out.println("initialImage="+initialImage+" initialImage.getWidth(null)="+initialImage.getWidth(null));
					coloredTexture = 
						new BufferedImage(
								initialImage.getWidth(null), 
								initialImage.getHeight(null), 
								BufferedImage.TYPE_INT_ARGB);
					Graphics gi = coloredTexture.getGraphics();
					gi.drawImage(initialImage, 0, 0, null);
				}*/
			}
			return coloredTexture;

		}

		static class ColorSwapFilter extends RGBImageFilter {
			private int target1;
			private int replacement1;
			private int target2;
			private int replacement2;

			public ColorSwapFilter(java.awt.Color target1, java.awt.Color replacement1, java.awt.Color target2, java.awt.Color replacement2) {
				this.target1 = target1.getRGB();
				this.replacement1 = replacement1.getRGB();
				this.target2 = target2.getRGB();
				this.replacement2 = replacement2.getRGB();
			}

			@Override
			public int filterRGB(int x, int y, int rgb) {
				// if (x==0 && y==0) logger.info("Starting convert image");
				// if (x==15 && y==15) logger.info("Finished convert image");
				if (rgb == target1) {
					return replacement1;
				} else if (rgb == target2) {
					return replacement2;
				}
				return rgb;
			}

			@Override
			public void imageComplete(int status) {
				super.imageComplete(status);
				if (logger.isLoggable(Level.FINE)) {
					logger.fine("imageComplete status=" + status);
				}
			}

		}

		@Override
		public Paint getPaint(GraphicalRepresentation<?> gr, double scale) {
			return new TexturePaint(getColoredTexture(), new Rectangle(0, 0, getColoredTexture().getWidth(), getColoredTexture()
					.getHeight()));
		}

		@Override
		public BackgroundStyleType getBackgroundStyleType() {
			return BackgroundStyleType.TEXTURE;
		}

		@Override
		public TextureType getTextureType() {
			return textureType;
		}

		@Override
		public void setTextureType(TextureType aTextureType) {
			if (requireChange(this.textureType, aTextureType)) {
				TextureType oldTexture = textureType;
				this.textureType = aTextureType;
				rebuildColoredTexture();
				setChanged();
				notifyObservers(new FGENotification(Parameters.textureType, oldTexture, aTextureType));
			}
		}

		@Override
		public java.awt.Color getColor1() {
			return color1;
		}

		@Override
		public void setColor1(java.awt.Color aColor) {
			if (requireChange(this.color1, aColor)) {
				java.awt.Color oldColor = color1;
				this.color1 = aColor;
				rebuildColoredTexture();
				setChanged();
				notifyObservers(new FGENotification(Parameters.color1, oldColor, aColor));
			}
		}

		@Override
		public java.awt.Color getColor2() {
			return color2;
		}

		@Override
		public void setColor2(java.awt.Color aColor) {
			if (requireChange(this.color2, aColor)) {
				java.awt.Color oldColor = color2;
				this.color2 = aColor;
				rebuildColoredTexture();
				setChanged();
				notifyObservers(new FGENotification(Parameters.color2, oldColor, aColor));
			}
		}

		@Override
		public String toString() {
			return "BackgroundStyle.TEXTURE(" + getColor1() + "," + getColor2() + "," + getTextureType() + ")";
		}

		private boolean requireChange(Object oldObject, Object newObject) {
			if (oldObject == null) {
				if (newObject == null) {
					return false;
				} else {
					return true;
				}
			}
			return !oldObject.equals(newObject);
		}
	}

	public static class BackgroundImageImpl extends BackgroundStyleImpl implements BackgroundImage {
		private File imageFile = null;
		private Image image = null;

		@Override
		public Paint getPaint(GraphicalRepresentation<?> gr, double scale) {
			return java.awt.Color.WHITE;
		}

		@Override
		public BackgroundStyleType getBackgroundStyleType() {
			return BackgroundStyleType.IMAGE;
		}

		@Override
		public File getImageFile() {
			return imageFile;
		}

		@Override
		public void setImageFile(File anImageFile) {
			if (requireChange(this.imageFile, anImageFile)) {
				File oldFile = imageFile;
				imageFile = anImageFile;
				if (anImageFile != null && anImageFile.exists()) {
					image = new ImageIcon(anImageFile.getAbsolutePath()).getImage();
				} else {
					image = null;
				}
				setChanged();
				notifyObservers(new FGENotification(Parameters.imageFile, oldFile, anImageFile));
			}
		}

		@Override
		public Image getImage() {
			return image;
		}

		@Override
		public void setImage(Image image) {
			this.image = image;
		}

		private ImageBackgroundType imageBackgroundType = ImageBackgroundType.TRANSPARENT;
		private double scaleX = 1.0;
		private double scaleY = 1.0;
		private double deltaX = 0.0;
		private double deltaY = 0.0;
		private java.awt.Color imageBackgroundColor = java.awt.Color.WHITE;
		private boolean fitToShape = false;

		@Override
		public java.awt.Color getImageBackgroundColor() {
			return imageBackgroundColor;
		}

		@Override
		public void setImageBackgroundColor(java.awt.Color aColor) {
			if (requireChange(this.imageBackgroundColor, aColor)) {
				java.awt.Color oldColor = imageBackgroundColor;
				this.imageBackgroundColor = aColor;
				setChanged();
				notifyObservers(new FGENotification(Parameters.imageBackgroundColor, oldColor, aColor));
			}
		}

		@Override
		public double getDeltaX() {
			return deltaX;
		}

		@Override
		public void setDeltaX(double aDeltaX) {
			if (requireChange(this.deltaX, aDeltaX)) {
				double oldDeltaX = this.deltaX;
				this.deltaX = aDeltaX;
				setChanged();
				notifyObservers(new FGENotification(Parameters.deltaX, oldDeltaX, deltaX));
			}
		}

		@Override
		public double getDeltaY() {
			return deltaY;
		}

		@Override
		public void setDeltaY(double aDeltaY) {
			if (requireChange(this.deltaY, aDeltaY)) {
				double oldDeltaY = this.deltaY;
				this.deltaY = aDeltaY;
				setChanged();
				notifyObservers(new FGENotification(Parameters.deltaY, oldDeltaY, deltaY));
			}
		}

		@Override
		public ImageBackgroundType getImageBackgroundType() {
			return imageBackgroundType;
		}

		@Override
		public void setImageBackgroundType(ImageBackgroundType anImageBackgroundType) {
			if (requireChange(this.imageBackgroundType, anImageBackgroundType)) {
				ImageBackgroundType oldImageBackgroundType = this.imageBackgroundType;
				this.imageBackgroundType = anImageBackgroundType;
				setChanged();
				notifyObservers(new FGENotification(Parameters.imageBackgroundType, oldImageBackgroundType, anImageBackgroundType));
			}
		}

		@Override
		public double getScaleX() {
			return scaleX;
		}

		@Override
		public void setScaleX(double aScaleX) {
			if (requireChange(this.scaleX, aScaleX)) {
				double oldScaleX = this.scaleX;
				// logger.info(toString()+": Sets scaleX from "+oldScaleX+" to "+aScaleX);
				this.scaleX = aScaleX;
				setChanged();
				notifyObservers(new FGENotification(Parameters.scaleX, oldScaleX, scaleX));
			}
		}

		@Override
		public void setScaleXNoNotification(double aScaleX) {
			if (requireChange(this.scaleX, aScaleX)) {
				this.scaleX = aScaleX;
			}
		}

		@Override
		public double getScaleY() {
			return scaleY;
		}

		@Override
		public void setScaleY(double aScaleY) {
			if (requireChange(this.scaleY, aScaleY)) {
				double oldScaleY = this.scaleY;
				// logger.info(toString()+": Sets scaleY from "+oldScaleY+" to "+aScaleY);
				this.scaleY = aScaleY;
				setChanged();
				notifyObservers(new FGENotification(Parameters.scaleY, oldScaleY, scaleY));
			}
		}

		@Override
		public void setScaleYNoNotification(double aScaleY) {
			if (requireChange(this.scaleY, aScaleY)) {
				this.scaleY = aScaleY;
			}
		}

		@Override
		public boolean getFitToShape() {
			return fitToShape;
		}

		@Override
		public void setFitToShape(boolean aFlag) {
			if (requireChange(this.fitToShape, aFlag)) {
				boolean oldValue = fitToShape;
				this.fitToShape = aFlag;
				setChanged();
				notifyObservers(new FGENotification(Parameters.fitToShape, oldValue, aFlag));
			}
		}

		@Override
		public String toString() {
			return "BackgroundStyle.IMAGE(" + getImageFile() + ")";
		}

		private boolean requireChange(Object oldObject, Object newObject) {
			if (oldObject == null) {
				if (newObject == null) {
					return false;
				} else {
					return true;
				}
			}
			return !oldObject.equals(newObject);
		}

	}

	@Override
	public float getTransparencyLevel() {
		return transparencyLevel;
	}

	@Override
	public void setTransparencyLevel(float aLevel) {
		if (requireChange(this.transparencyLevel, aLevel)) {
			float oldValue = transparencyLevel;
			this.transparencyLevel = aLevel;
			setChanged();
			notifyObservers(new FGENotification(Parameters.transparencyLevel, oldValue, aLevel));
		}
	}

	@Override
	public boolean getUseTransparency() {
		return useTransparency;
	}

	@Override
	public void setUseTransparency(boolean aFlag) {
		if (requireChange(this.useTransparency, aFlag)) {
			boolean oldValue = useTransparency;
			this.useTransparency = aFlag;
			setChanged();
			notifyObservers(new FGENotification(Parameters.useTransparency, oldValue, aFlag));
		}
	}

	@Override
	public BackgroundStyleImpl clone() {
		try {
			BackgroundStyleImpl returned = (BackgroundStyleImpl) super.clone();
			return returned;
		} catch (CloneNotSupportedException e) {
			// cannot happen since we are clonable
			e.printStackTrace();
			return null;
		}
	}

	private boolean requireChange(Object oldObject, Object newObject) {
		if (oldObject == null) {
			if (newObject == null) {
				return false;
			} else {
				return true;
			}
		}
		return !oldObject.equals(newObject);
	}

}
