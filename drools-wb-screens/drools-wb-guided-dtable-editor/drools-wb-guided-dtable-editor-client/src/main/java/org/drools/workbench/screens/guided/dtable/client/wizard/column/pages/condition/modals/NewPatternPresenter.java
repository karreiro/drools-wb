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

package org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.condition.modals;

import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.drools.workbench.models.guided.dtable.shared.model.BRLRuleModel;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableConstants;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.condition.PatternWizardPage;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.uberfire.client.mvp.UberElement;

@Dependent
public class NewPatternPresenter {

    private final View view;

    private PatternWizardPage patternWizardPage;

    @Inject
    public NewPatternPresenter( View view ) {
        this.view = view;
    }

    public void show() {
        view.clear();
        view.show();
    }

    public void hide() {
        view.hide();
    }

    @PostConstruct
    public void setup() {
        view.init( this );
    }

    public void init( final PatternWizardPage patternWizardPage ) {
        this.patternWizardPage = patternWizardPage;
    }

    public List<String> getFactTypes() {
        return Arrays.asList( oracle().getFactTypes() );
    }

    void cancel() {
        view.hide();
    }

    void addPattern() {
        if ( !isValid() ) {
            return;
        }

        setTheNewEditingPattern();
        clearEditingCol();
        updatePatternPageView();
        hide();
    }

    private void updatePatternPageView() {
        patternWizardPage.updateNewPatternLabel();
    }

    private boolean isValid() {
        if ( !isNegatePatternMatch() ) {
            if ( factName().equals( "" ) ) {
                view.showError( GuidedDecisionTableConstants.INSTANCE.PleaseEnterANameForFact() );
                return false;
            } else if ( factName().equals( factType() ) ) {
                view.showError( GuidedDecisionTableConstants.INSTANCE.PleaseEnterANameThatIsNotTheSameAsTheFactType() );
                return false;
            } else if ( !isBindingUnique( factName() ) ) {
                view.showError( GuidedDecisionTableConstants.INSTANCE.PleaseEnterANameThatIsNotAlreadyUsedByAnotherPattern() );
                return false;
            }
        }

        return true;
    }

    private void setTheNewEditingPattern() {
        patternWizardPage.plugin().setEditingPattern( newPattern52() );
    }

    private Pattern52 newPattern52() {
        final Pattern52 editingPattern = new Pattern52();

        editingPattern.setFactType( factType() );
        editingPattern.setBoundName( factName() );
        editingPattern.setNegated( isNegatePatternMatch() );

        return editingPattern;
    }

    private String factName() {
        return isNegatePatternMatch() ? "" : view.getBindingText();
    }

    private String factType() {
        return view.getSelectedFactType();
    }

    private boolean isNegatePatternMatch() {
        return view.isNegatePatternMatch();
    }

    private void clearEditingCol() {
        patternWizardPage.plugin().setEditingColFactField( null );
        patternWizardPage.plugin().setEditingColOperator( null );
    }

    private boolean isBindingUnique( String binding ) {
        final BRLRuleModel brlRuleModel = new BRLRuleModel( model() );

        return !brlRuleModel.isVariableNameUsed( binding );
    }

    private AsyncPackageDataModelOracle oracle() {
        return patternWizardPage.presenter().getDataModelOracle();
    }

    private GuidedDecisionTable52 model() {
        return patternWizardPage.presenter().getModel();
    }

    public interface View extends UberElement<NewPatternPresenter> {

        void show();

        void hide();

        void clear();

        boolean isNegatePatternMatch();

        String getSelectedFactType();

        String getBindingText();

        void showError( String errorMessage );
    }
}
