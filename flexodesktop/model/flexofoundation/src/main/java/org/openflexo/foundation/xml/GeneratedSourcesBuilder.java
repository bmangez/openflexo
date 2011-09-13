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
package org.openflexo.foundation.xml;

import java.util.logging.Logger;

import org.openflexo.foundation.rm.FlexoGeneratedOutputResource;
import org.openflexo.foundation.sg.GeneratedSources;


/**
 * Used only during XML serialization to build generated output
 * 
 * @author bmangez
 */
public class GeneratedSourcesBuilder extends FlexoBuilder<FlexoGeneratedOutputResource>
{
    @SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(GeneratedSourcesBuilder.class.getPackage().getName());

    public GeneratedSources generatedSources;

    public GeneratedSourcesBuilder(FlexoGeneratedOutputResource resource)
    {
        super(resource);
    }

}
