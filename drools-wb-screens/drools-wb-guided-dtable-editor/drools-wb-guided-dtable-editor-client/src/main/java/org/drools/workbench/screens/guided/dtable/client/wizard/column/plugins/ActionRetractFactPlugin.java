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

package org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.Window;
import org.drools.workbench.models.guided.dtable.shared.model.ActionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.ActionRetractFactCol52;
import org.drools.workbench.models.guided.dtable.shared.model.DTCellValue52;
import org.drools.workbench.models.guided.dtable.shared.model.DTColumnConfig52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryActionRetractFactCol52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryCol;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.NewGuidedDecisionTableColumnWizard;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasAdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.AdditionalInfoPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.PatternToDeletePage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.AdditionalInfoPageInitializer;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.BaseDecisionTableColumnPlugin;
import org.uberfire.ext.widgets.core.client.wizards.WizardPage;

import static org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.DecisionTableColumnViewUtils.nil;

@Dependent
public class ActionRetractFactPlugin extends BaseDecisionTableColumnPlugin implements HasAdditionalInfoPage {

    @Inject
    private PatternToDeletePage patternToDeletePage;

    @Inject
    private AdditionalInfoPage additionalInfoPage;

    private ActionRetractFactCol52 editingCol;

    private Boolean patternToDeletePageCompleted = Boolean.FALSE;

    @Override
    public void init(final NewGuidedDecisionTableColumnWizard wizard) {
        super.init(wizard);

        setupDefaultValues();
    }

    @Override
    public String getTitle() {
        return translate(GuidedDecisionTableErraiConstants.ActionRetractFactPlugin_DeleteAnExistingFact);
    }

    @Override
    public List<WizardPage> getPages() {
        return new ArrayList<WizardPage>() {{
            if (tableFormat() == GuidedDecisionTable52.TableFormat.LIMITED_ENTRY) {
                add(patternToDeletePage);
            }

            add(additionalInfoPage());
        }};
    }

    @Override
    public Boolean generateColumn() {
        if (!isValid()) {
            return false;
        }

        presenter.appendColumn(editingCol);

        return true;
    }

    @Override
    public DTColumnConfig52 editingCol() {
        return editingCol;
    }

    @Override
    public String getHeader() {
        return null;
    }

    @Override
    public void setHeader(final String header) {
        editingCol().setHeader(header);

        fireChangeEvent(additionalInfoPage);
    }

    @Override
    public void setInsertLogical(Boolean value) {

    }

    @Override
    public void setUpdate(Boolean value) {

    }

    public boolean unique(final String header) {
        for (final ActionCol52 col52 : presenter.getModel().getActionCols()) {
            if (col52.getHeader().equals(header)) {
                return false;
            }
        }

        return true;
    }

    public String getEditingColStringValue() {
        return getEditingColValue().getStringValue();
    }

    public void setEditingColStringValue(final String pattern) {
        getEditingColValue().setStringValue(pattern);
    }

    public void setPatternToDeletePageAsCompleted() {
        if (!isPatternToDeletePageCompleted()) {
            setPatternToDeletePageCompleted(Boolean.TRUE);

            fireChangeEvent(patternToDeletePage);
        }
    }

    public Boolean isPatternToDeletePageCompleted() {
        return patternToDeletePageCompleted;
    }

    void setupDefaultValues() {
        editingCol = clone(newActionRetractFactColumn());
    }

    boolean isValid() {
        final String header = editingCol().getHeader();

        if (nil(header)) {
            showError(translate(GuidedDecisionTableErraiConstants.ActionRetractFactPlugin_YouMustEnterAColumnHeaderValueDescription));
            return false;
        }

        if (!unique(header)) {
            showError(translate(GuidedDecisionTableErraiConstants.ActionRetractFactPlugin_ThatColumnNameIsAlreadyInUsePleasePickAnother));
            return false;
        }

        return true;
    }

    void setPatternToDeletePageCompleted(Boolean completed) {
        patternToDeletePageCompleted = completed;
    }

    void showError(final String message) {
        Window.alert(message);
    }

    private AdditionalInfoPage additionalInfoPage() {
        return AdditionalInfoPageInitializer.init(additionalInfoPage,
                                                  this);
    }

    private DTCellValue52 getEditingColValue() {
        return getLimitedEntryActionRetractFactCol52().getValue();
    }

    private LimitedEntryActionRetractFactCol52 getLimitedEntryActionRetractFactCol52() {
        return (LimitedEntryActionRetractFactCol52) editingCol();
    }

    private ActionRetractFactCol52 newActionRetractFactColumn() {
        switch (tableFormat()) {
            case EXTENDED_ENTRY:
                return new ActionRetractFactCol52();
            case LIMITED_ENTRY:
                final LimitedEntryActionRetractFactCol52 col52 = new LimitedEntryActionRetractFactCol52();

                col52.setValue(new DTCellValue52(""));

                return col52;
            default:
                throw new UnsupportedOperationException("Unsupported table format: " + tableFormat());
        }
    }

    private ActionRetractFactCol52 clone(final ActionRetractFactCol52 col52) {
        final ActionRetractFactCol52 clone;

        if (col52 instanceof LimitedEntryCol) {
            clone = new LimitedEntryActionRetractFactCol52();

            final DTCellValue52 oldValue = ((LimitedEntryCol) col52).getValue();
            final DTCellValue52 newValue = new DTCellValue52(oldValue.getStringValue());

            ((LimitedEntryCol) clone).setValue(newValue);
        } else {
            clone = new ActionRetractFactCol52();
        }

        clone.setHeader(col52.getHeader());
        clone.setHideColumn(col52.isHideColumn());

        return clone;
    }

    private GuidedDecisionTable52.TableFormat tableFormat() {
        return presenter.getModel().getTableFormat();
    }
}