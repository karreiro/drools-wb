/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
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

package org.drools.workbench.screens.guided.dtable.client.editor.page;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.drools.workbench.models.guided.dtable.shared.model.ActionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.AttributeCol52;
import org.drools.workbench.models.guided.dtable.shared.model.BaseColumn;
import org.drools.workbench.models.guided.dtable.shared.model.CompositeColumn;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.models.guided.dtable.shared.model.MetadataCol52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableModellerView;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.widget.table.accordion.GuidedDecisionTableAccordion;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.control.AttributeColumnConfigRow;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.control.ColumnLabelWidget;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.control.ColumnManagementView;
import org.drools.workbench.screens.guided.dtable.client.widget.table.columns.control.DeleteColumnManagementAnchorWidget;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.RefreshActionsPanelEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.RefreshAttributesPanelEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.RefreshConditionsPanelEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.RefreshMetaDataPanelEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.ModelSynchronizer;
import org.drools.workbench.screens.guided.dtable.client.widget.table.utilities.ColumnUtilities;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.jboss.errai.common.client.ui.ElementWrapperWidget;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.uberfire.backend.vfs.ObservablePath;
import org.uberfire.client.mvp.UpdatedLockStatusEvent;
import org.uberfire.client.views.pfly.multipage.PageImpl;
import org.uberfire.ext.widgets.common.client.common.popups.errors.ErrorPopup;

import static org.drools.workbench.screens.guided.dtable.client.widget.table.accordion.GuidedDecisionTableAccordionItem.Type;
import static org.drools.workbench.screens.guided.dtable.client.widget.table.accordion.GuidedDecisionTableAccordionItem.Type.ACTION;
import static org.drools.workbench.screens.guided.dtable.client.widget.table.accordion.GuidedDecisionTableAccordionItem.Type.ATTRIBUTE;
import static org.drools.workbench.screens.guided.dtable.client.widget.table.accordion.GuidedDecisionTableAccordionItem.Type.CONDITION;
import static org.drools.workbench.screens.guided.dtable.client.widget.table.accordion.GuidedDecisionTableAccordionItem.Type.METADATA;

public class ColumnsPage extends PageImpl {

    private static final String TAB_TITLE = "Columns";

    // Injected

    private final GuidedDecisionTableAccordion accordion;

    private final ManagedInstance<AttributeColumnConfigRow> attributeColumnConfigRows;

    private final TranslationService translationService;

    private final ManagedInstance<DeleteColumnManagementAnchorWidget> deleteColumnManagementAnchorWidgets;

    private final ColumnManagementView conditionsPanel;

    private final ColumnManagementView actionsPanel;

    // Constructed

    private GuidedDecisionTableModellerView.Presenter modeller;

    private VerticalPanel attributeWidget;

    private VerticalPanel metaDataWidget;

    private VerticalPanel conditionsWidget;

    private VerticalPanel actionsWidget;

    @Inject
    public ColumnsPage(final GuidedDecisionTableAccordion accordion,
                       final ManagedInstance<AttributeColumnConfigRow> attributeColumnConfigRows,
                       final TranslationService translationService,
                       final ManagedInstance<DeleteColumnManagementAnchorWidget> deleteColumnManagementAnchorWidgets,
                       final ColumnManagementView conditionsPanel,
                       final ColumnManagementView actionsPanel) {

        super(asWidget(accordion), TAB_TITLE);

        this.accordion = accordion;
        this.attributeColumnConfigRows = attributeColumnConfigRows;
        this.translationService = translationService;
        this.deleteColumnManagementAnchorWidgets = deleteColumnManagementAnchorWidgets;
        this.conditionsPanel = conditionsPanel;
        this.actionsPanel = actionsPanel;

        setupAccordionWidgets();
    }

    private static ElementWrapperWidget<?> asWidget(final GuidedDecisionTableAccordion accordion) {

        final GuidedDecisionTableAccordion.View view = accordion.getView();

        return ElementWrapperWidget.getWidget(view.getElement());
    }

    public void onUpdatedLockStatusEvent(final @Observes UpdatedLockStatusEvent event) {

        final GuidedDecisionTableView.Presenter activeDecisionTable = modeller.getActiveDecisionTable();

        if (activeDecisionTable == null) {
            return;
        }

        final ObservablePath currentPath = activeDecisionTable.getCurrentPath();

        if (currentPath.equals(event.getFile())) {
            refreshAttributeWidget();
            refreshMetaDataWidget();
            refreshConditionsWidget();
            refreshActionsWidget();
        }
    }

    public void onRefreshAttributesPanelEvent(final @Observes RefreshAttributesPanelEvent event) {
        refreshAttributeWidget();
    }

    public void onRefreshMetaDataPanelEvent(final @Observes RefreshMetaDataPanelEvent event) {
        refreshMetaDataWidget();
    }

    public void onRefreshConditionsPanelEvent(final @Observes RefreshConditionsPanelEvent event) {
        refreshConditionsWidget();
    }

    public void onRefreshActionsPanelEvent(final @Observes RefreshActionsPanelEvent event) {
        refreshActionsWidget();
    }

    public void init(final GuidedDecisionTableModellerView.Presenter modeller) {

        this.modeller = modeller;

        this.accordion.init(modeller);
        this.actionsPanel.init(modeller);
        this.conditionsPanel.init(modeller);
    }

    private void setupAccordionWidgets() {
        setupAccordionWidget(ATTRIBUTE, this::setAttributeWidget);
        setupAccordionWidget(METADATA, this::setMetaDataWidget);
        setupAccordionWidget(CONDITION, this::setConditionsWidget);
        setupAccordionWidget(ACTION, this::setActionsWidget);
    }

    private void setupAccordionWidget(final Type accordionType,
                                      final Consumer<VerticalPanel> setWidget) {

        final VerticalPanel defaultPanel = makeDefaultPanel();

        setWidget.accept(defaultPanel);

        accordion.addItem(accordionType, defaultPanel);
    }

    @Override
    public void onFocus() {
        refreshAttributeWidget();
        refreshMetaDataWidget();
        refreshConditionsWidget();
        refreshActionsWidget();

        accordion.init(modeller);
    }

    private void refreshAttributeWidget() {

        final List<AttributeCol52> attributeColumns = getGuidedDecisionTable52().getAttributeCols();
        final VerticalPanel attributeWidget = getAttributeWidget();

        attributeWidget.clear();

        if (attributeColumns.isEmpty()) {
            attributeWidget.add(blankSlate());
            return;
        }

        for (final AttributeCol52 attributeColumn : attributeColumns) {

            final AttributeColumnConfigRow row = attributeColumnConfigRows.get();

            row.init(attributeColumn, modeller);
            attributeWidget.add(row.getView());
        }
    }

    private void refreshMetaDataWidget() {

        final List<MetadataCol52> metaDataColumns = getGuidedDecisionTable52().getMetadataCols();
        final VerticalPanel metaDataWidget = getMetaDataWidget();

        metaDataWidget.clear();

        if (metaDataColumns.isEmpty()) {
            metaDataWidget.add(blankSlate());
            return;
        }

        final boolean isEditable = modeller.isActiveDecisionTableEditable();
        for (MetadataCol52 metaDataColumn : metaDataColumns) {
            HorizontalPanel hp = new HorizontalPanel();
            hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

            final ColumnLabelWidget label = makeColumnLabel(metaDataColumn);
            hp.add(label);

            final MetadataCol52 originalColumn = metaDataColumn;
            final CheckBox chkHideColumn = new CheckBox(new StringBuilder(GuidedDecisionTableConstants.INSTANCE.HideThisColumn()).append(GuidedDecisionTableConstants.COLON).toString());
            chkHideColumn.setValue(metaDataColumn.isHideColumn());
            chkHideColumn.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(final ClickEvent event) {
                    final MetadataCol52 editedColumn = originalColumn.cloneColumn();
                    editedColumn.setHideColumn(chkHideColumn.getValue());
                    try {
                        modeller.getActiveDecisionTable().updateColumn(originalColumn,
                                                                       editedColumn);
                    } catch (ModelSynchronizer.VetoException veto) {
                        showGenericVetoMessage();
                    }
                }
            });

            hp.add(chkHideColumn);

            if (isEditable) {
                final DeleteColumnManagementAnchorWidget deleteWidget = deleteColumnManagementAnchorWidgets.get();
                deleteWidget.init(metaDataColumn.getMetadata(),
                                  () -> {
                                      try {
                                          modeller.getActiveDecisionTable().deleteColumn(metaDataColumn);
                                      } catch (ModelSynchronizer.VetoException veto) {
                                          showGenericVetoMessage();
                                      }
                                  });
                hp.add(deleteWidget);
            }

            metaDataWidget.add(hp);
        }
    }

    private void refreshConditionsWidget() {

        final List<CompositeColumn<? extends BaseColumn>> conditionColumns = getGuidedDecisionTable52().getConditions();
        getConditionsWidget().clear();

        if (conditionColumns == null || conditionColumns.isEmpty()) {
            getAccordion().getItem(CONDITION).setOpen(false);
            getConditionsWidget().add(blankSlate());
            return;
        }

        getConditionsWidget().add(conditionsPanel);

        final Map<String, List<BaseColumn>> columnGroups =
                conditionColumns.stream().collect(
                        Collectors.groupingBy(
                                DecisionTableColumnViewUtils::getColumnManagementGroupTitle,
                                Collectors.toList()
                        )
                );
        conditionsPanel.renderColumns(columnGroups);
    }

    private void refreshActionsWidget() {

        final List<ActionCol52> actionColumns = getGuidedDecisionTable52().getActionCols();

        getActionsWidget().clear();

        if (actionColumns == null || actionColumns.isEmpty()) {
            getAccordion().getItem(ACTION).setOpen(false);
            getActionsWidget().add(blankSlate());
            return;
        }

        //Each Action is a row in a vertical panel
        getActionsWidget().add(actionsPanel);

        //Add Actions to panel
        final Map<String, List<BaseColumn>> columnGroups =
                actionColumns.stream().collect(
                        Collectors.groupingBy(
                                DecisionTableColumnViewUtils::getColumnManagementGroupTitle,
                                Collectors.toList()
                        )
                );

        actionsPanel.renderColumns(columnGroups);
    }

    private void showGenericVetoMessage() {
        ErrorPopup.showMessage(translate(GuidedDecisionTableErraiConstants.NewGuidedDecisionTableColumnWizard_GenericVetoError));
    }

    private ColumnLabelWidget makeColumnLabel(final MetadataCol52 metaDataColumn) {
        final ColumnLabelWidget label = new ColumnLabelWidget(metaDataColumn.getMetadata());
        ColumnUtilities.setColumnLabelStyleWhenHidden(label,
                                                      metaDataColumn.isHideColumn());
        return label;
    }

    private String translate(final String key,
                             final Object... args) {
        return translationService.format(key,
                                         args);
    }

    private VerticalPanel getAttributeWidget() {
        return attributeWidget;
    }

    private void setAttributeWidget(final VerticalPanel attributeWidget) {
        this.attributeWidget = attributeWidget;
    }

    private VerticalPanel getMetaDataWidget() {
        return metaDataWidget;
    }

    private void setMetaDataWidget(final VerticalPanel metaDataWidget) {
        this.metaDataWidget = metaDataWidget;
    }

    private VerticalPanel getConditionsWidget() {
        return conditionsWidget;
    }

    private void setConditionsWidget(final VerticalPanel conditionsWidget) {
        this.conditionsWidget = conditionsWidget;
    }

    private VerticalPanel getActionsWidget() {
        return actionsWidget;
    }

    private void setActionsWidget(final VerticalPanel actionsWidget) {
        this.actionsWidget = actionsWidget;
    }

    private GuidedDecisionTable52 getGuidedDecisionTable52() {

        final GuidedDecisionTableView.Presenter activeDecisionTable = modeller.getActiveDecisionTable();

        return activeDecisionTable.getModel();
    }

    private VerticalPanel makeDefaultPanel() {
        return new VerticalPanel() {{
            add(blankSlate());
        }};
    }

    private Label blankSlate() {

        final String disabledLabelStyle = "text-muted";
        final String noColumns = GuidedDecisionTableConstants.INSTANCE.NoColumnsAvailable();

        return new Label() {{
            setText(noColumns);
            setStyleName(disabledLabelStyle);
        }};
    }

    public GuidedDecisionTableAccordion getAccordion() {
        return accordion;
    }
}
