/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common;

import java.util.function.Supplier;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import org.gwtbootstrap3.client.ui.html.Div;

public class FormFieldWrapper<T extends IsWidget> {

    final Label label;
    final Div container;

    public FormFieldWrapper(final Label label,
                            final Div container) {
        this.label = label;
        this.container = container;
    }

    public T setup(final Boolean canSetup,
                   final Supplier<T> widget) {
        formFieldToggle(canSetup);

        if (!canSetup) {
            return null;
        }

        return setupWidget(widget.get());
    }

    private T setupWidget(final T widget) {
        addWidgetToContainer(widget);

        return widget;
    }

    public void formFieldToggle(final Boolean visible) {
        label.setVisible(visible);
        container.setVisible(visible);
    }

    private void addWidgetToContainer(final IsWidget widget) {
        container.clear();
        container.add(widget);
    }
}
