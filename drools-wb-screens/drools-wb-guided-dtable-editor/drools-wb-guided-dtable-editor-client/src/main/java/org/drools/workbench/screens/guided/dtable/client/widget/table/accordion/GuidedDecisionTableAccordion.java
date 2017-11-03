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

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.screens.guided.dtable.client.resources.GuidedDecisionTableResources;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableModellerView;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.DecisionTableSelectedEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.DecisionTableSelectionsChangedEvent;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.NewGuidedDecisionTableColumnWizard;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.kie.workbench.common.widgets.client.ruleselector.RuleSelector;
import org.uberfire.client.mvp.UberElement;
import org.uberfire.client.mvp.UpdatedLockStatusEvent;
import org.uberfire.commons.uuid.UUID;

@Dependent
public class GuidedDecisionTableAccordion {

    private final View view;

    private final ManagedInstance<GuidedDecisionTableAccordionItem> itemManagedInstance;

    private final List<GuidedDecisionTableAccordionItem> items = new ArrayList<>();

    private final ManagedInstance<NewGuidedDecisionTableColumnWizard> wizardManagedInstance;

    private String parentId;

    private GuidedDecisionTableModellerView.Presenter modeller;
    private RuleSelector ruleSelector;

    @Inject
    public GuidedDecisionTableAccordion(final View view,
                                        final ManagedInstance<GuidedDecisionTableAccordionItem> itemManagedInstance,
                                        final ManagedInstance<NewGuidedDecisionTableColumnWizard> wizardManagedInstance) {
        this.view = view;
        this.itemManagedInstance = itemManagedInstance;
        this.wizardManagedInstance = wizardManagedInstance;
    }

    @PostConstruct
    public void setup() {
        view.init(this);

        setupParentId();
    }

    private void setupRuleInheritance() {
        view.setRuleInheritance(new FlowPanel() {{
            add(ruleInheritanceWidget());
        }});
    }

    public void init(final GuidedDecisionTableModellerView.Presenter modeller) {
        this.modeller = modeller;

        setupRuleInheritance();
        setColumnsNoteInfoHidden(modeller.getActiveDecisionTable().hasColumnDefinitions());
    }

    private void setupParentId() {
        parentId = UUID.uuid();

        view.setParentId(parentId);
    }

    public View getView() {
        return view;
    }

    public void addItem(final GuidedDecisionTableAccordionItem.Type type,
                        final Widget widget) {
        addItem(makeItem(type, widget));
    }

    public void openNewGuidedDecisionTableColumnWizard() {

        if (!isColumnCreationEnabledToActiveDecisionTable()) {
            return;
        }

        final NewGuidedDecisionTableColumnWizard wizard = wizardManagedInstance.get();
        final GuidedDecisionTableView.Presenter activeDecisionTable = modeller.getActiveDecisionTable();

        wizard.init(activeDecisionTable);
        wizard.start();
    }

    public boolean isColumnCreationEnabledToActiveDecisionTable() {

        final Optional<GuidedDecisionTableView.Presenter> decisionTable = Optional.ofNullable(modeller.getActiveDecisionTable());

        if (!decisionTable.isPresent()) {
            return false;
        }

        return isColumnCreationEnabled(decisionTable.get());
    }

    boolean isColumnCreationEnabled(final GuidedDecisionTableView.Presenter dtPresenter) {

        final boolean decisionTableIsEditable = !dtPresenter.isReadOnly();
        final boolean decisionTableHasEditableColumns = dtPresenter.hasEditableColumns();

        return decisionTableHasEditableColumns && decisionTableIsEditable;
    }

    public void onDecisionTableSelected(final @Observes DecisionTableSelectedEvent event) {

        final Optional<GuidedDecisionTableView.Presenter> dtPresenter = event.getPresenter();

        if (modeller == null) {
            return;
        }

        if (!dtPresenter.isPresent()) {
            return;
        }

        final GuidedDecisionTableView.Presenter presenter = dtPresenter.get();

        if (presenter.equals(modeller.getActiveDecisionTable())) {
            return;
        }

        setupRuleSelector(presenter);
    }

    private void setupRuleSelector(final GuidedDecisionTableView.Presenter presenter) {

        if (presenter == null) {
            return;
        }

        final GuidedDecisionTable52 model = presenter.getModel();
        final String ruleName = model.getParentName();

        presenter.getPackageParentRuleNames(availableParentRuleNames -> {
            ruleSelector.setRuleName(ruleName);
            ruleSelector.setRuleNames(availableParentRuleNames);
        });
    }

    Widget ruleInheritanceWidget() {

        final FlowPanel result = makeFlowPanel();

        result.setStyleName(GuidedDecisionTableResources.INSTANCE.css().ruleInheritance());
        result.add(ruleInheritanceLabel());
        result.add(ruleSelector());

        return result;
    }

    FlowPanel makeFlowPanel() {
        return new FlowPanel();
    }

    Widget ruleSelector() {
//        makeRuleSelector().setEnabled(false);

        this.ruleSelector = makeRuleSelector();

        ruleSelector.addValueChangeHandler(e -> {
            modeller.getActiveDecisionTable().setParentRuleName(e.getValue());
        });

        setupRuleSelector(modeller.getActiveDecisionTable());

        return ruleSelector;
    }

    Label ruleInheritanceLabel() {
        final Label label = new Label(GuidedDecisionTableConstants.INSTANCE.AllTheRulesInherit());

        label.setStyleName(GuidedDecisionTableResources.INSTANCE.css().ruleInheritanceLabel());

        return label;
    }

    RuleSelector makeRuleSelector() {
        return new RuleSelector();
    }

    private GuidedDecisionTableAccordionItem makeItem(final GuidedDecisionTableAccordionItem.Type type,
                                                      final Widget widget) {
        final GuidedDecisionTableAccordionItem accordionItem = blankAccordionItem();

        accordionItem.init(getParentId(),
                           type,
                           widget);

        return accordionItem;
    }

    GuidedDecisionTableAccordionItem blankAccordionItem() {
        return itemManagedInstance.get();
    }

    List<GuidedDecisionTableAccordionItem> getItems() {
        return items;
    }

    private void addItem(final GuidedDecisionTableAccordionItem item) {
        getItems().add(item);
        getView().addItem(item);
    }

    public GuidedDecisionTableAccordionItem getItem(final GuidedDecisionTableAccordionItem.Type type) {
        return getItems()
                .stream()
                .filter(item -> item.getType() == type)
                .findFirst()
                .orElse(blankAccordionItem());
    }

    public void setColumnsNoteInfoHidden(final boolean isHidden) {
        view.setColumnsNoteInfoHidden(isHidden);
    }

    String getParentId() {
        return parentId;
    }

    public interface View extends UberElement<GuidedDecisionTableAccordion> {

        void setColumnsNoteInfoHidden(final boolean isHidden);

        void addItem(final GuidedDecisionTableAccordionItem item);

        void setParentId(final String parentId);

        void setRuleInheritance(IsWidget widget);
    }
}
