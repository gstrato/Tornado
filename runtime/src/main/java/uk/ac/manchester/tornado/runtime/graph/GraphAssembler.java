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
package uk.ac.manchester.tornado.runtime.graph;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import uk.ac.manchester.tornado.runtime.common.RuntimeUtilities;

public class GraphAssembler {

    // @formatter:off 
    public enum TornadoVMBytecodes {
        
        ALLOCATE  ((byte)10),   // ALLOCATE(obj,dest)
        COPY_IN   ((byte)11),   // COPY(obj, src, dest)
        STREAM_IN ((byte)12),   // STREAM_IN(obj, src, dest)
        STREAM_OUT((byte)13),   // STREAM_OUT(obj, src, dest)
        STREAM_OUT_BLOCKING((byte)14), // STREAM_OUT(obj, src, dest)
        LAUNCH    ((byte)15),   // LAUNCH(dep list index)
        BARRIER   ((byte)16),
        SETUP     ((byte)17),  
        BEGIN     ((byte)18),   // BEGIN(num contexts, num stacks, num dep lists)
        ADD_DEP   ((byte)19),   // ADD_DEP(list index)
        CONTEXT   ((byte)20),   // CONTEXT(ctx)
        END       ((byte)21),
        CONSTANT_ARG ((byte)22),
        REFERENCE_ARG((byte)23);
        
        private byte index;
        
        TornadoVMBytecodes(byte index) {
            this.index = index;
        }
        
        public byte index() {
            return index;
        }
        
    }
    // @formatter:on

    // public final static byte ALLOCATE = 20; // ALLOCATE(obj,dest)
    // public final static byte COPY_IN = 21; // COPY(obj, src, dest)
    // public final static byte STREAM_IN = 22; // STREAM_IN(obj, src, dest)
    // public final static byte STREAM_OUT = 23; // STREAM_OUT(obj, src, dest)
    // public final static byte STREAM_OUT_BLOCKING = 24; // STREAM_OUT(obj,
    // src,
    // // dest)
    //
    // public final static byte LAUNCH = 28; // LAUNCH(dep list index)
    //
    // public final static byte BARRIER = 50;
    //
    // public final static byte SETUP = 10; // BEGIN(num contexts, num stacks,
    // num
    // // dep lists)
    //
    // public final static byte BEGIN = 11;
    // public final static byte ADD_DEP = 12; // ADD_DEP(list index)
    // public final static byte CONTEXT = 13; // CONTEXT(ctx)
    // public final static byte END = 0;
    //
    // public final static byte CONSTANT_ARG = 32;
    // public final static byte REFERENCE_ARG = 33;

    private final ByteBuffer buffer;

    public GraphAssembler(byte[] code) {
        buffer = ByteBuffer.wrap(code);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    public void reset() {
        buffer.rewind();
    }

    public int position() {
        return buffer.position();
    }

    public void begin() {
        buffer.put(TornadoVMBytecodes.BEGIN.index());
    }

    public void end() {
        buffer.put(TornadoVMBytecodes.END.index());
    }

    public void setup(int numContexts, int numStacks, int numDeps) {
        buffer.put(TornadoVMBytecodes.SETUP.index());
        buffer.putInt(numContexts);
        buffer.putInt(numStacks);
        buffer.putInt(numDeps);
    }

    public void addDependency(int index) {
        buffer.put(TornadoVMBytecodes.ADD_DEP.index());
        buffer.putInt(index);
    }

    public void context(int index) {
        buffer.put(TornadoVMBytecodes.CONTEXT.index());
        buffer.putInt(index);
    }

    public void allocate(int object, int ctx) {
        buffer.put(TornadoVMBytecodes.ALLOCATE.index);
        buffer.putInt(object);
        buffer.putInt(ctx);
    }

    public void copyToContext(int obj, int ctx, int dep) {
        buffer.put(TornadoVMBytecodes.COPY_IN.index);
        buffer.putInt(obj);
        buffer.putInt(ctx);
        buffer.putInt(dep);
    }

    public void streamInToContext(int obj, int ctx, int dep) {
        buffer.put(TornadoVMBytecodes.STREAM_IN.index());
        buffer.putInt(obj);
        buffer.putInt(ctx);
        buffer.putInt(dep);
    }

    public void streamOutOfContext(int obj, int ctx, int dep) {
        buffer.put(TornadoVMBytecodes.STREAM_OUT.index());
        buffer.putInt(obj);
        buffer.putInt(ctx);
        buffer.putInt(dep);
    }

    public void launch(int gtid, int ctx, int task, int numParameters, int dep) {
        buffer.put(TornadoVMBytecodes.LAUNCH.index());
        buffer.putInt(gtid);
        buffer.putInt(ctx);
        buffer.putInt(task);
        buffer.putInt(numParameters);
        buffer.putInt(dep);
    }

    public void barrier(int dep) {
        buffer.put(TornadoVMBytecodes.BARRIER.index());
        buffer.putInt(dep);
    }

    public void constantArg(int index) {
        buffer.put(TornadoVMBytecodes.CONSTANT_ARG.index());
        buffer.putInt(index);
    }

    public void referenceArg(int index) {
        buffer.put(TornadoVMBytecodes.REFERENCE_ARG.index());
        buffer.putInt(index);
    }

    public void dump() {
        final int width = 16;
        System.out.printf("code  : capacity = %s, in use = %s \n", RuntimeUtilities.humanReadableByteCount(buffer.capacity(), true), RuntimeUtilities.humanReadableByteCount(buffer.position(), true));
        for (int i = 0; i < buffer.position(); i += width) {
            System.out.printf("[0x%04x]: ", i);
            for (int j = 0; j < Math.min(buffer.capacity() - i, width); j++) {
                if (j % 2 == 0) {
                    System.out.printf(" ");
                }
                if (j < buffer.position() - i) {
                    System.out.printf("%02x", buffer.get(i + j));
                } else {
                    System.out.printf("..");
                }
            }
            System.out.println();
        }
    }

}
