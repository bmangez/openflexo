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
package org.openflexo.wkf.processeditor;

import java.awt.Font;
import java.util.logging.Logger;

import org.openflexo.foundation.utils.FlexoFont;
import org.openflexo.foundation.wkf.FlexoPetriGraph;
import org.openflexo.foundation.wkf.FlexoProcess;
import org.openflexo.foundation.wkf.WKFAnnotation;
import org.openflexo.foundation.wkf.WKFArtefact;
import org.openflexo.foundation.wkf.WKFDataObject;
import org.openflexo.foundation.wkf.WKFDataSource;
import org.openflexo.foundation.wkf.WKFObject;
import org.openflexo.foundation.wkf.WKFStockObject;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.wkf.processeditor.gr.AnnotationGR;
import org.openflexo.wkf.processeditor.gr.DataObjectGR;
import org.openflexo.wkf.processeditor.gr.DataSourceGR;
import org.openflexo.wkf.processeditor.gr.StockObjectGR;

public class ArtefactPalette extends AbstractWKFPalette {

	private static final Logger logger = Logger.getLogger(ArtefactPalette.class.getPackage().getName());

	private ContainerValidity DROP_ON_PETRI_GRAPH = new ContainerValidity() {
		@Override
		public boolean isContainerValid(WKFObject container) {
			return container instanceof FlexoPetriGraph || container instanceof FlexoProcess || container instanceof WKFArtefact;
		}
	};

	private WKFPaletteElement dataFile;

	private WKFPaletteElement dataSource;

	private WKFPaletteElement annotation;

	private WKFPaletteElement boundingBox;

	private WKFPaletteElement stockArtefact;

	public ArtefactPalette() {
		super(300, 130, "artefact");

		dataFile = makeDataFileElement(40, 15);
		dataSource = makeDataSourceElement(120, 15);
		stockArtefact = makeStockElement(190, 15);
		annotation = makeAnnotationElement(50, 95);
		boundingBox = makeBoundingBoxElement(135, 95, 150, 80);
		makePalettePanel();
	}

	private WKFPaletteElement makeAnnotationElement(int x, int y) {
		WKFAnnotation annotation = new WKFAnnotation((FlexoProcess) null);
		annotation.setText(FlexoLocalization.localizedForKey("process_annotation"));
		annotation.setX(x, ProcessEditorConstants.BASIC_PROCESS_EDITOR);
		annotation.setY(y, ProcessEditorConstants.BASIC_PROCESS_EDITOR);
		annotation.setIsAnnotation();
		annotation.setTextFont(new FlexoFont("Lucida Sans", Font.ITALIC, 10));
		annotation.setIsRounded(false);
		return makePaletteElement(annotation, new AnnotationGR(annotation, null), DROP_ON_PETRI_GRAPH);
	}

	private WKFPaletteElement makeBoundingBoxElement(int x, int y, int width, int height) {
		final WKFAnnotation annotation = new WKFAnnotation((FlexoProcess) null);
		annotation.setText(FlexoLocalization.localizedForKey("bounding_box"));
		annotation.setX(x, ProcessEditorConstants.BASIC_PROCESS_EDITOR);
		annotation.setY(y, ProcessEditorConstants.BASIC_PROCESS_EDITOR);
		annotation.setWidth(width, ProcessEditorConstants.BASIC_PROCESS_EDITOR);
		annotation.setHeight(height, ProcessEditorConstants.BASIC_PROCESS_EDITOR);
		annotation.setIsBoundingBox();
		annotation.setIsRounded(true);
		annotation.setTextFont(new FlexoFont("Lucida Sans", Font.ITALIC, 9));
		/*annotation.setIsSolidBackground(false);
		annotation.setIsFloatingLabel(true);*/
		annotation.setLabelX(60, ProcessEditorConstants.BASIC_PROCESS_EDITOR);
		annotation.setLabelY(10, ProcessEditorConstants.BASIC_PROCESS_EDITOR);
		// annotation.setTextFont(new FlexoFont("SansSerif",Font.PLAIN, 9), ActivityNodeGR.SWIMMING_LANE_EDITOR);
		return makePaletteElement(annotation, new AnnotationGR(annotation, null), DROP_ON_PETRI_GRAPH);
	}

	public WKFPaletteElement makeDataSourceElement(int x, int y) {
		WKFDataSource annotation = new WKFDataSource((FlexoProcess) null);
		annotation.setText(FlexoLocalization.localizedForKey("data_source"));
		annotation.setX(x, ProcessEditorConstants.BASIC_PROCESS_EDITOR);
		annotation.setY(y, ProcessEditorConstants.BASIC_PROCESS_EDITOR);
		annotation.setLabelX(20, ProcessEditorConstants.BASIC_PROCESS_EDITOR);
		annotation.setLabelY(50, ProcessEditorConstants.BASIC_PROCESS_EDITOR);
		annotation.setTextFont(new FlexoFont("Lucida Sans", Font.PLAIN, 10));
		return makePaletteElement(annotation, new DataSourceGR(annotation, null), DROP_ON_PETRI_GRAPH);

	}

	public WKFPaletteElement makeDataFileElement(int x, int y) {
		WKFDataObject annotation = new WKFDataObject((FlexoProcess) null);
		annotation.setText(FlexoLocalization.localizedForKey("document"));
		annotation.setX(x, ProcessEditorConstants.BASIC_PROCESS_EDITOR);
		annotation.setY(y, ProcessEditorConstants.BASIC_PROCESS_EDITOR);
		annotation.setLabelX(20, ProcessEditorConstants.BASIC_PROCESS_EDITOR);
		annotation.setLabelY(50, ProcessEditorConstants.BASIC_PROCESS_EDITOR);
		annotation.setTextFont(new FlexoFont("Lucida Sans", Font.PLAIN, 10));
		return makePaletteElement(annotation, new DataObjectGR(annotation, null), DROP_ON_PETRI_GRAPH);

	}

	public WKFPaletteElement makeStockElement(int x, int y) {
		WKFStockObject annotation = new WKFStockObject((FlexoProcess) null);
		annotation.setText(FlexoLocalization.localizedForKey("stock"));
		annotation.setX(x, ProcessEditorConstants.BASIC_PROCESS_EDITOR);
		annotation.setY(y, ProcessEditorConstants.BASIC_PROCESS_EDITOR);
		annotation.setTextFont(new FlexoFont("Lucida Sans", Font.PLAIN, 10));
		annotation.setLabelX(15, ProcessEditorConstants.BASIC_PROCESS_EDITOR);
		annotation.setLabelY(45, ProcessEditorConstants.BASIC_PROCESS_EDITOR);
		return makePaletteElement(annotation, new StockObjectGR(annotation, null), DROP_ON_PETRI_GRAPH);

	}

	public WKFPaletteElement getDataFile() {
		return dataFile;
	}

	public WKFPaletteElement getDataSource() {
		return dataSource;
	}

	public WKFPaletteElement getAnnotation() {
		return annotation;
	}

	public WKFPaletteElement getBoundingBox() {
		return boundingBox;
	}
}
