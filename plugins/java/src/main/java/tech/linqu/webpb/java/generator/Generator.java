/*
 * Copyright (c) 2020 linqu.tech, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.linqu.webpb.java.generator;

import static com.google.protobuf.Descriptors.EnumDescriptor;
import static com.google.protobuf.Descriptors.FileDescriptor;
import static tech.linqu.webpb.utilities.utils.DescriptorUtils.resolveFileDescriptor;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.LineComment;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.Descriptor;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;
import tech.linqu.webpb.java.utils.ImportLookup;
import tech.linqu.webpb.utilities.context.RequestContext;
import tech.linqu.webpb.utilities.utils.Const;

/**
 * Generator to process {@link Descriptor}.
 */
public final class Generator {

    private final ImportLookup importLookup = new ImportLookup();

    private RequestContext requestContext;

    /**
     * Create a generator.
     *
     * @return {@link Generator}
     */
    public static Generator create() {
        return new Generator();
    }

    /**
     * Generate from {@link RequestContext}.
     *
     * @param context {@link RequestContext}
     * @return {@link Map}
     */
    public Map<String, String> generate(RequestContext context) {
        this.requestContext = context;
        this.importLookup
            .update(resolveFileDescriptor(context.getDescriptors(), Const.WEBPB_OPTIONS));

        Map<String, String> fileMap = new TreeMap<>();
        for (FileDescriptor fileDescriptor : context.getTargetDescriptors()) {
            String javaPackage = fileDescriptor.getOptions().getJavaPackage();
            if (shouldIgnore(javaPackage)) {
                continue;
            }
            for (Descriptors.Descriptor descriptor : fileDescriptor.getMessageTypes()) {
                CompilationUnit unit = generateMessage(fileDescriptor, descriptor);
                fileMap.put(filename(javaPackage, descriptor.getName()), unit.toString());
            }
            for (Descriptors.EnumDescriptor enumDescriptor : fileDescriptor.getEnumTypes()) {
                CompilationUnit unit = generateEnum(fileDescriptor, enumDescriptor);
                fileMap.put(filename(javaPackage, enumDescriptor.getName()), unit.toString());
            }
        }
        return fileMap;
    }

    private static String filename(String javaPackage, String className) {
        return javaPackage.replaceAll("\\.", "/") + "/" + className + ".java";
    }

    private static boolean shouldIgnore(String packageName) {
        return StringUtils.isEmpty(packageName)
            || packageName.startsWith("com.google.protobuf")
            || packageName.startsWith("tech.linqu.webpb.utilities.descriptor");
    }

    /**
     * Generator entrance for {@link Descriptor}.
     *
     * @param fileDescriptor {@link FileDescriptor}
     * @param descriptor     {@link Descriptor}
     * @return {@link CompilationUnit}
     */
    public CompilationUnit generateMessage(FileDescriptor fileDescriptor, Descriptor descriptor) {
        CompilationUnit compilationUnit = createCompilationUnit(fileDescriptor);
        return new MessageGenerator()
            .generate(compilationUnit, requestContext, importLookup, fileDescriptor, descriptor);
    }

    /**
     * Generator entrance for {@link EnumDescriptor}.
     *
     * @param fileDescriptor {@link FileDescriptor}
     * @param enumDescriptor {@link EnumDescriptor}
     * @return {@link CompilationUnit}
     */
    public CompilationUnit generateEnum(FileDescriptor fileDescriptor,
                                        EnumDescriptor enumDescriptor) {
        CompilationUnit compilationUnit = createCompilationUnit(fileDescriptor);
        return new EnumGenerator().generate(compilationUnit, enumDescriptor);
    }

    private CompilationUnit createCompilationUnit(FileDescriptor fileDescriptor) {
        CompilationUnit unit = new CompilationUnit();
        unit.addOrphanComment(new LineComment(Const.HEADER));
        unit.addOrphanComment(new LineComment(Const.GIT_URL));
        String javaPackage = fileDescriptor.getOptions().getJavaPackage();
        unit.setPackageDeclaration(javaPackage);
        return unit;
    }
}
