/*******************************************************************************
 * Copyright (c) 2010-2014, Gabor Szarnyas, Istvan Rath and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Gabor Szarnyas - initial API and implementation
 *******************************************************************************/
package hu.bme.mit.incqueryd.engine.rete.messages;

import java.io.Serializable;

public class BenchmarkConfigMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	protected int size;
	protected int series;

	public BenchmarkConfigMessage(final int size, final int series) {
		super();
		this.size = size;
		this.series = series;
	}

	public int getSize() {
		return size;
	}

	public int getSeries() {
		return series;
	}

}
