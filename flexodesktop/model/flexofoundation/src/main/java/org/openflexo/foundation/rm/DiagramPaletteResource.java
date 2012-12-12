package org.openflexo.foundation.rm;

import org.openflexo.foundation.resource.FlexoXMLFileResource;
import org.openflexo.foundation.viewpoint.DiagramPalette;
import org.openflexo.foundation.viewpoint.ViewPointLibrary;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;

@ModelEntity
@ImplementationClass(ExampleDiagramResourceImpl.class)
@XMLElement
public interface DiagramPaletteResource extends FlexoXMLFileResource<DiagramPalette> {

	public static final String VIEW_POINT_LIBRARY = "viewPointLibrary";

	public DiagramPalette getDiagramPalette();

	@Getter(value = VIEW_POINT_LIBRARY, ignoreType = true)
	public ViewPointLibrary getViewPointLibrary();

	@Setter(VIEW_POINT_LIBRARY)
	public void setViewPointLibrary(ViewPointLibrary viewPointLibrary);

	@Override
	public ViewPointResource getContainer();
}