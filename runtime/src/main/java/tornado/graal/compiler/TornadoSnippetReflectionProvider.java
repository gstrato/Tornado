/* 
 * Copyright 2012 James Clarkson.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tornado.graal.compiler;

import org.graalvm.compiler.api.replacements.SnippetReflectionProvider;
import jdk.vm.ci.meta.JavaConstant;
import jdk.vm.ci.meta.JavaKind;
import jdk.vm.ci.meta.ResolvedJavaType;

import static tornado.common.exceptions.TornadoInternalError.unimplemented;

public class TornadoSnippetReflectionProvider implements SnippetReflectionProvider {

    @Override
    public <T> T asObject(Class<T> type, JavaConstant jc) {
        unimplemented();
        return null;
    }

    @Override
    public Object asObject(ResolvedJavaType type, JavaConstant constant) {
        unimplemented();
        return null;
    }

    @Override
    public JavaConstant forBoxed(JavaKind kind, Object value) {
        unimplemented();
        return null;
    }

    @Override
    public JavaConstant forObject(Object object) {
        unimplemented();
        return null;
    }

    @Override
    public <T> T getInjectedNodeIntrinsicParameter(Class<T> type) {
        unimplemented();
        return null;
    }

}