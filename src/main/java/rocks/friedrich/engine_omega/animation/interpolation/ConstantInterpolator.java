/*
 * Source: https://github.com/engine-alpha/engine-alpha/blob/4.x/engine-alpha/src/main/java/ea/animation/interpolation/ConstantInterpolator.java
 *
 * Engine Omega ist eine anfängerorientierte 2D-Gaming Engine.
 *
 * Copyright (c) 2011 - 2024 Michael Andonie and contributors.
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
package rocks.friedrich.engine_omega.animation.interpolation;

import rocks.friedrich.engine_omega.animation.Interpolator;
import rocks.friedrich.engine_omega.annotations.API;
import rocks.friedrich.engine_omega.annotations.Internal;

/**
 * Ein Interpolator, der eine konstante Funktion darstellt.
 *
 * @param <Value> Ein beliebiger Typ zum Interpolieren
 */
public class ConstantInterpolator<Value> implements Interpolator<Value>
{
    private final Value value;

    /**
     * Erstellt einen konstanten Interpolator
     *
     * @param value Der stets auszugebende Wert
     */
    @API
    public ConstantInterpolator(Value value)
    {
        this.value = value;
    }

    @Internal
    @Override
    public Value interpolate(double progress)
    {
        return value;
    }
}
