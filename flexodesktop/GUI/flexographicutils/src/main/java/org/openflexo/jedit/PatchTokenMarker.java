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
package org.openflexo.jedit;

/*
 * PatchTokenMarker.java - DIFF patch token marker
 * Copyright (C) 1999 Slava Pestov
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */

import javax.swing.text.Segment;

/**
 * Patch/diff token marker.
 * 
 * @author Slava Pestov
 * @version $Id: PatchTokenMarker.java,v 1.2 2011/09/12 11:47:09 gpolet Exp $
 */
public class PatchTokenMarker extends TokenMarker {
	@Override
	public byte markTokensImpl(byte token, Segment line, int lineIndex) {
		if (line.count == 0)
			return Token.NULL;
		switch (line.array[line.offset]) {
		case '+':
		case '>':
			addToken(line.count, Token.KEYWORD1);
			break;
		case '-':
		case '<':
			addToken(line.count, Token.KEYWORD2);
			break;
		case '@':
		case '*':
			addToken(line.count, Token.KEYWORD3);
			break;
		default:
			addToken(line.count, Token.NULL);
			break;
		}
		return Token.NULL;
	}

	@Override
	public boolean supportsMultilineTokens() {
		return false;
	}
}
