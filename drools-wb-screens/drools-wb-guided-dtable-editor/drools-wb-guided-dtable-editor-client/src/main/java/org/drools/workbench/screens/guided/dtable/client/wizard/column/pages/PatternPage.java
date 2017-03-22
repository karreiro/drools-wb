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

package org.drools.workbench.screens.guided.dtable.client.wizard.column.pages;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.drools.workbench.models.guided.dtable.shared.model.BRLRuleModel;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.drools.workbench.screens.guided.dtable.client.resources.i18n.GuidedDecisionTableErraiConstants;
import org.drools.workbench.screens.guided.dtable.client.widget.table.GuidedDecisionTableView;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.commons.HasPatternPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common.BaseDecisionTableColumnPage;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.modals.NewPatternPresenter;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons.DecisionTableColumnPlugin;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.client.mvp.UberView;

@Dependent
public class PatternPage<T extends HasPatternPage & DecisionTableColumnPlugin> extends BaseDecisionTableColumnPage<T> {

    @Inject
    private View view;

    @Inject
    private NewPatternPresenter newPatternPresenter;

    private SimplePanel content = new SimplePanel();

    @Override
    public void initialise() {
        newPatternPresenter.init(this);

        content.setWidget(view);
    }

    @Override
    public String getTitle() {
        return translate(GuidedDecisionTableErraiConstants.PatternPage_Pattern);
    }

    @Override
    public void isComplete(final Callback<Boolean> callback) {
        callback.callback(plugin().editingPattern() != null);
    }

    @Override
    public void prepareView() {
        view.init(this);
    }

    @Override
    public Widget asWidget() {
        return content;
    }

    public GuidedDecisionTableView.Presenter presenter() {
        return presenter;
    }

    public void setEditingPattern(final Pattern52 pattern52) {
        if (pattern52 != null) {
            pattern52.setEntryPointName(view.getEntryPointName());
        }

        plugin().setEditingPattern(pattern52);
    }

    public void disableEntryPoint() {
        view.disableEntryPoint();
    }

    void setSelectedEditingPattern() {
        final String selectedValue = view.getSelectedValue();
        final String[] metadata = selectedValue.split("\\s");
        final Pattern52 editingPattern = extractEditingPattern(metadata);

        setEditingPattern(editingPattern);
    }

    void forEachPattern(final BiConsumer<String, String> biConsumer) {
        final Set<String> addedBounds = new HashSet<>();

        getPatterns()
                .forEach(pattern52 -> {
                    if (!addedBounds.contains(pattern52.getBoundName())) {
                        final String patternName = patternToName(pattern52);
                        final String patternValue = patternToValue(pattern52);

                        biConsumer.accept(patternName,
                                          patternValue);

                        addedBounds.add(pattern52.getBoundName());
                    }
                });
    }

    Pattern52 extractEditingPattern(final String[] metadata) {
        final GuidedDecisionTable52 model = presenter.getModel();
        final String factType = metadata[0];
        final String factBinding = metadata[1];
        final boolean negated = Boolean.parseBoolean(metadata[2]);

        if (!negated) {
            return model
                    .getConditionPattern(factBinding);
        } else {
            return model
                    .getPatterns()
                    .stream()
                    .filter(Pattern52::isNegated)
                    .filter(p -> p.getFactType().equals(factType))
                    .findFirst()
                    .orElseThrow(IllegalStateException::new);
        }
    }

    void showNewPatternModal() {
        newPatternPresenter.show();
    }

    String currentPatternName() {
        final Pattern52 currentPattern = plugin().editingPattern();

        return currentPattern == null ? "" : patternToName(currentPattern);
    }

    String currentPatternValue() {
        final Pattern52 currentPattern = plugin().editingPattern();

        return currentPattern == null ? "" : patternToValue(currentPattern);
    }

    List<Pattern52> getPatterns() {
        final List<Pattern52> patterns = new ArrayList<>(presenter.getModel().getPatterns());
        final Pattern52 currentPattern = plugin().editingPattern();

        if (currentPattern != null) {
            patterns.add(currentPattern);
        }

        return patterns;
    }

    String getEntryPointName() {
        return plugin().getEntryPointName();
    }

    void setEntryPoint() {
        plugin().setEntryPointName(view.getEntryPointName());
    }

    private String patternToValue(final Pattern52 pattern52) {
        return pattern52.getFactType() + " " + pattern52.getBoundName() + " " + pattern52.isNegated();
    }

    private String patternToName(final Pattern52 pattern52) {
        final String not = translate(GuidedDecisionTableErraiConstants.PatternPage_NegatedPattern);
        final String prefix = pattern52.isNegated() ? not + " " : "";

        return prefix + pattern52.getFactType() + " [" + pattern52.getBoundName() + "]";
    }

    public interface View extends UberView<PatternPage> {

        void setup();

        String getSelectedValue();

        String getEntryPointName();

        void disableEntryPoint();
    }
}
