/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
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

package org.drools.workbench.screens.guided.dtable.client.widget.table.accordion;

import javax.inject.Inject;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.IsWidget;
import org.gwtbootstrap3.client.ui.Button;
import org.jboss.errai.common.client.dom.DOMUtil;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.common.client.dom.HTMLElement;
import org.jboss.errai.common.client.dom.UnorderedList;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

@Templated
public class GuidedDecisionTableAccordionView implements GuidedDecisionTableAccordion.View,
                                                         IsElement {

    @DataField("items")
    private UnorderedList items;

    @DataField("columnsNoteInfo")
    private Div columnsNoteInfo;

    @DataField("insert-column")
    private Button insertColumn;

    @DataField("ruleInheritanceContainer")
    private Div ruleInheritanceContainer;

    private ManagedInstance<GuidedDecisionTableAccordionItem> itemManagedInstance;

    private GuidedDecisionTableAccordion presenter;

    @Inject
    public GuidedDecisionTableAccordionView(final UnorderedList items,
                                            final Div columnsNoteInfo,
                                            final ManagedInstance<GuidedDecisionTableAccordionItem> itemManagedInstance,
                                            final Button insertColumn,
                                            final Div ruleInheritanceContainer) {
        this.items = items;
        this.columnsNoteInfo = columnsNoteInfo;
        this.itemManagedInstance = itemManagedInstance;
        this.insertColumn = insertColumn;
        this.ruleInheritanceContainer = ruleInheritanceContainer;
    }

    @Override
    public void init(final GuidedDecisionTableAccordion presenter) {
        this.presenter = presenter;
        this.columnsNoteInfo.setHidden(true);
    }

    @EventHandler("insert-column")
    public void onInsertColumnClick(final ClickEvent event) {
        presenter.openNewGuidedDecisionTableColumnWizard();
    }

    @Override
    public void setColumnsNoteInfoHidden(final boolean isHidden) {
        columnsNoteInfo.setHidden(isHidden);
    }

    @Override
    public void addItem(final GuidedDecisionTableAccordionItem item) {
        items.appendChild(getViewElement(item));
    }

    @Override
    public void setParentId(final String parentId) {
        items.setId(parentId);
    }

    @Override
    public void setRuleInheritance(final IsWidget widget) {
        DOMUtil.removeAllChildren(ruleInheritanceContainer);
        DOMUtil.appendWidgetToElement(ruleInheritanceContainer, widget);
    }

    private HTMLElement getViewElement(final GuidedDecisionTableAccordionItem accordionItem) {
        final GuidedDecisionTableAccordionItem.View view = accordionItem.getView();

        return view.getElement();
    }
}
