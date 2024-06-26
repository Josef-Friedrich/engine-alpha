/*
 * Source: https://github.com/engine-alpha/engine-alpha/blob/4.x/engine-alpha/src/main/java/ea/internal/annotations/API.java
 *
 * Engine Pi ist eine anfängerorientierte 2D-Gaming Engine.
 *
 * Copyright (c) 2011 - 2014 Michael Andonie and contributors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.pirckheimer_gymnasium.engine_pi.annotations;

import java.lang.annotation.Documented;

/**
 * Diese Annotation markiert Methoden, die Schüler verwenden sollen. Methoden
 * ohne <code>@API</code> sollen nicht verwendet werden!
 * <p>
 * Bisher müssen diese Methoden auch mit
 * <code>@SuppressWarnings("unused")</code> zusätzlich markiert werden.
 *
 * @author Niklas Keller
 */
@Documented
public @interface API
{
}
