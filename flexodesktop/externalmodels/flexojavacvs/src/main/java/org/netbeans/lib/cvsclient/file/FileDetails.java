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
package org.netbeans.lib.cvsclient.file;

import java.io.File;

/**
 * A wrapper class that describes a file.
 * @author  Robert Greig
 */
public class FileDetails {
    /**
     * The file
     */
    private File file;

    /**
     * Whether the file is binary
     */
    private boolean isBinary;

    /**
     * Construct a FileDetails object
     * @param theFile the file
     * @param binary true if the file is binary, false if it is text
     */
    public FileDetails(File file, boolean isBinary) {
        this.file = file;
        this.isBinary = isBinary;
    }

    /**
     * Return the file.
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * Return the file type.
     * @return true if the file is binary, false if it is text
     */
    public boolean isBinary() {
        return isBinary;
    }
}