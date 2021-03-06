/*
 * This file is part of Tornado: A heterogeneous programming framework: 
 * https://github.com/beehive-lab/tornado
 *
 * Copyright (c) 2013-2018, APT Group, School of Computer Science,
 * The University of Manchester. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Authors: James Clarkson
 *
 */
package uk.ac.manchester.tornado.drivers.opencl.enums;

public class OCLMemFlags {
    public static final long CL_MEM_READ_WRITE = (1 << 0);
    public static final long CL_MEM_WRITE_ONLY = (1 << 1);
    public static final long CL_MEM_READ_ONLY = (1 << 2);
    public static final long CL_MEM_USE_HOST_PTR = (1 << 3);
    public static final long CL_MEM_ALLOC_HOST_PTR = (1 << 4);
    public static final long CL_MEM_COPY_HOST_PTR = (1 << 5);
    // reserved (1 << 6)
    public static final long CL_MEM_HOST_WRITE_ONLY = (1 << 7);
    public static final long CL_MEM_HOST_READ_ONLY = (1 << 8);
    public static final long CL_MEM_HOST_NO_ACCESS = (1 << 9);
}
