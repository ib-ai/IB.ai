package com.ibdiscord.exceptions;

/** Copyright 2019 Jarred Vardy <jarredvardy@gmail.com>
 *
 * This file is part of CORAL.
 *
 * CORAL is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CORAL is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CORAL. If not, see http://www.gnu.org/licenses/.
 */
public class LocaliserSyntaxException extends Exception {

    private static final String syntaxErrorMessage = "The Localiser expects a category.key pair separated by a full-stop. Number of keys/categories passed: ";

    /**
     * Creates a localiser syntax exception.
     * @param message The message to append.
     */
    public LocaliserSyntaxException(String message) {
        super(syntaxErrorMessage + message);
    }

}
