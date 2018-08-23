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
package uk.ac.manchester.tornado.examples.memory;

import uk.ac.manchester.tornado.api.TaskSchedule;
import uk.ac.manchester.tornado.api.annotations.Parallel;
import uk.ac.manchester.tornado.api.common.TornadoDevice;
import uk.ac.manchester.tornado.api.mm.TornadoMemoryProvider;
import uk.ac.manchester.tornado.api.runtime.TornadoRuntime;

public class DeviceMemoryTest {

    public static String humanReadableByteCount(long bytes, boolean si) {
        final int unit = si ? 1000 : 1024;
        if (bytes < unit) {
            return bytes + " B";
        }
        final int exp = (int) (Math.log(bytes) / Math.log(unit));
        final String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");

        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static void main(final String[] args) {

        TornadoDevice device = TornadoRuntime.getTornadoRuntime().getDefaultDevice();
        final TornadoMemoryProvider mm = device.getDeviceContext().getMemoryManager();

        final long heapSize = mm.getHeapSize() - 1024;

        final int numWords = (int) (heapSize >> 2);

        System.out.printf("device memory test:\n\tdevice: %s\n\tmax heap=%s\n\tnum words=%d\n", device.getDevice().getName(), humanReadableByteCount(heapSize, false), numWords);

        final int[] data = new int[numWords];

        final TaskSchedule schedule = new TaskSchedule("s0").streamIn(data).task("t0", DeviceMemoryTest::fill, data).streamOut(data);

        schedule.warmup();

        intialise(data);
        schedule.execute();

        validate(data);

    }

    private static void fill(int[] data) {
        for (@Parallel int i = 0; i < data.length; i++) {
            data[i] = i;
        }
    }

    private static void intialise(int[] data) {
        for (int i = 0; i < data.length; i++) {
            data[i] = 0;
        }
    }

    private static void validate(int[] data) {
        int errors = 0;
        int first = -1;
        for (int i = 0; i < data.length; i++) {
            if (data[i] != i) {
                errors++;
                if (first == -1) {
                    first = i;
                }
            }
        }

        System.out.printf("data=%s, errors=%d, first=%d\n", humanReadableByteCount(data.length << 2, false), errors, first);
    }

}
