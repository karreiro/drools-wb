/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
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

package org.drools.workbench.screens.guided.rule.client.editor.factPattern;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import org.drools.workbench.models.datamodel.oracle.DataType;
import org.drools.workbench.models.datamodel.oracle.FieldAccessorsAndMutators;
import org.drools.workbench.models.datamodel.oracle.ModelField;
import org.drools.workbench.models.datamodel.rule.CompositeFieldConstraint;
import org.drools.workbench.models.datamodel.rule.ExpressionField;
import org.drools.workbench.models.datamodel.rule.ExpressionFormLine;
import org.drools.workbench.models.datamodel.rule.ExpressionUnboundFact;
import org.drools.workbench.models.datamodel.rule.FactPattern;
import org.drools.workbench.models.datamodel.rule.HasConstraints;
import org.drools.workbench.models.datamodel.rule.SingleFieldConstraint;
import org.drools.workbench.models.datamodel.rule.SingleFieldConstraintEBLeftSide;
import org.drools.workbench.screens.guided.rule.client.editor.BindingTextBox;
import org.drools.workbench.screens.guided.rule.client.editor.RuleModeller;
import org.drools.workbench.screens.guided.rule.client.resources.GuidedRuleEditorResources;
import org.drools.workbench.screens.guided.rule.client.resources.images.GuidedRuleEditorImages508;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.TextBox;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.kie.workbench.common.widgets.client.resources.i18n.HumanReadableConstants;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.ext.widgets.common.client.common.InfoPopup;
import org.uberfire.ext.widgets.common.client.common.SmallLabel;
import org.uberfire.ext.widgets.common.client.common.popups.FormStylePopup;

public class PopupCreator {

    private FactPattern pattern;
    private AsyncPackageDataModelOracle oracle;
    private RuleModeller modeller;
    private boolean bindable;

    /**
     * Returns the pattern.
     */
    public FactPattern getPattern() {
        return pattern;
    }

    /**
     * @param pattern the pattern to set
     */
    public void setPattern(FactPattern pattern) {
        this.pattern = pattern;
    }

    /**
     * Returns the oracle.
     */
    public AsyncPackageDataModelOracle getDataModelOracle() {
        return oracle;
    }

    /**
     * @param oracle the oracle to set
     */
    public void setDataModelOracle(AsyncPackageDataModelOracle oracle) {
        this.oracle = oracle;
    }

    /**
     * Returns the modeller.
     */
    public RuleModeller getModeller() {
        return modeller;
    }

    /**
     * @param modeller the modeller to set
     */
    public void setModeller(RuleModeller modeller) {
        this.modeller = modeller;
    }

    /**
     * Returns the bindable.
     */
    public boolean isBindable() {
        return bindable;
    }

    /**
     * @param bindable the bindable to set
     */
    public void setBindable(boolean bindable) {
        this.bindable = bindable;
    }

    /**
     * Display a little editor for field bindings.
     */
    public void showBindFieldPopup(final FactPattern fp,
                                   final SingleFieldConstraint con,
                                   final ModelField[] fields,
                                   final PopupCreator popupCreator) {
        final FormStylePopup popup = new FormStylePopup(GuidedRuleEditorResources.CONSTANTS.AddAField());
//        popup.setWidth( 500 + "px" );
//        popup.setHeight( 100 + "px" );
        final HorizontalPanel vn = new HorizontalPanel();
        final TextBox varName = new BindingTextBox();
        if (con.getFieldBinding() != null) {
            varName.setText(con.getFieldBinding());
        }
        final Button ok = new Button(HumanReadableConstants.INSTANCE.Set());
        vn.add(varName);
        vn.add(ok);

        ok.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                String var = varName.getText();
                if (modeller.isVariableNameUsed(var)) {
                    Window.alert(GuidedRuleEditorResources.CONSTANTS.TheVariableName0IsAlreadyTaken(var));
                    return;
                }
                con.setFieldBinding(var);
                modeller.refreshWidget();
                popup.hide();
            }
        });
        popup.addAttribute(GuidedRuleEditorResources.CONSTANTS.BindTheFieldCalled0ToAVariable(con.getFieldName()),
                           vn);

        //Show the sub-field selector is there are applicable sub-fields
        if (hasApplicableFields(fields)) {
            Button sub = new Button(GuidedRuleEditorResources.CONSTANTS.ShowSubFields());
            popup.addAttribute(GuidedRuleEditorResources.CONSTANTS.ApplyAConstraintToASubFieldOf0(con.getFieldName()),
                               sub);
            sub.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    popup.hide();
                    popupCreator.showPatternPopup(fp,
                                                  con
                    );
                }
            });
        }

        popup.show();
    }

    //Check if there are any fields other than "this"
    private boolean hasApplicableFields(final ModelField[] fields) {
        if (fields == null || fields.length == 0) {
            return false;
        }
        if (fields.length > 1) {
            return true;
        }
        if (DataType.TYPE_THIS.equals(fields[0].getName())) {
            return false;
        }
        return true;
    }

    /**
     * This shows a popup for adding fields to a composite
     */
    public void showPatternPopupForComposite(final HasConstraints hasConstraints) {
        final FormStylePopup popup = new FormStylePopup(GuidedRuleEditorImages508.INSTANCE.Wizard(),
                                                        GuidedRuleEditorResources.CONSTANTS.AddFieldsToThisConstraint());

        final ListBox box = new ListBox();
        box.addItem("...");
        this.oracle.getFieldCompletions(this.pattern.getFactType(),
                                        new Callback<ModelField[]>() {

                                            @Override
                                            public void callback(final ModelField[] fields) {
                                                for (int i = 0; i < fields.length; i++) {
                                                    final String fieldName = fields[i].getName();
                                                    box.addItem(fieldName);
                                                }
                                            }
                                        });

        box.setSelectedIndex(0);

        box.addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                String factType = pattern.getFactType();
                String fieldName = box.getItemText(box.getSelectedIndex());
                String fieldType = getDataModelOracle().getFieldType(factType,
                                                                     fieldName);
                hasConstraints.addConstraint(new SingleFieldConstraint(factType,
                                                                       fieldName,
                                                                       fieldType,
                                                                       null));
                modeller.refreshWidget();
                popup.hide();
            }
        });
        popup.addAttribute(GuidedRuleEditorResources.CONSTANTS.AddARestrictionOnAField(),
                           box);

        final ListBox composites = new ListBox();
        composites.addItem("..."); //NON-NLS
        composites.addItem(GuidedRuleEditorResources.CONSTANTS.AllOfAnd(),
                           CompositeFieldConstraint.COMPOSITE_TYPE_AND);
        composites.addItem(GuidedRuleEditorResources.CONSTANTS.AnyOfOr(),
                           CompositeFieldConstraint.COMPOSITE_TYPE_OR);
        composites.setSelectedIndex(0);

        composites.addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                CompositeFieldConstraint comp = new CompositeFieldConstraint();
                comp.setCompositeJunctionType(composites.getValue(composites.getSelectedIndex()));
                hasConstraints.addConstraint(comp);
                modeller.refreshWidget();
                popup.hide();
            }
        });

        InfoPopup infoComp = new InfoPopup(GuidedRuleEditorResources.CONSTANTS.MultipleFieldConstraints(),
                                           GuidedRuleEditorResources.CONSTANTS.MultipleConstraintsTip());

        HorizontalPanel horiz = new HorizontalPanel();
        horiz.add(composites);
        horiz.add(infoComp);
        popup.addAttribute(GuidedRuleEditorResources.CONSTANTS.MultipleFieldConstraint(),
                           horiz);

        //Include Expression Editor
        popup.addRow(new SmallLabel("<i>" + GuidedRuleEditorResources.CONSTANTS.AdvancedOptionsColon() + "</i>"));

        Button predicate = new Button(GuidedRuleEditorResources.CONSTANTS.NewFormula());
        predicate.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                SingleFieldConstraint con = new SingleFieldConstraint();
                con.setConstraintValueType(SingleFieldConstraint.TYPE_PREDICATE);
                hasConstraints.addConstraint(con);
                modeller.refreshWidget();
                popup.hide();
            }
        });
        popup.addAttribute(GuidedRuleEditorResources.CONSTANTS.AddANewFormulaStyleExpression(),
                           predicate);

        Button ebBtn = new Button(GuidedRuleEditorResources.CONSTANTS.ExpressionEditor());

        ebBtn.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                SingleFieldConstraintEBLeftSide con = new SingleFieldConstraintEBLeftSide();
                con.setConstraintValueType(SingleFieldConstraint.TYPE_UNDEFINED);
                con.setExpressionLeftSide(new ExpressionFormLine(new ExpressionUnboundFact(pattern.getFactType())));
                hasConstraints.addConstraint(con);
                modeller.refreshWidget();
                popup.hide();
            }
        });
        popup.addAttribute(GuidedRuleEditorResources.CONSTANTS.ExpressionEditor(),
                           ebBtn);

        popup.show();
    }

    /**
     * This shows a popup allowing you to add field constraints to a pattern
     * (its a popup).
     */
    public void showPatternPopup(final FactPattern factPattern,
                                 final SingleFieldConstraint fieldConstraint) {

        final boolean isNested = fieldConstraint != null;
        final String title = isNested ? GuidedRuleEditorResources.CONSTANTS.ModifyConstraintsFor0(factPattern.getFactType()) : GuidedRuleEditorResources.CONSTANTS.AddSubFieldConstraint();
        final String factType = getFactType(factPattern,
                                            fieldConstraint);
        final FormStylePopup popup = new FormStylePopup(GuidedRuleEditorImages508.INSTANCE.Wizard(),
                                                        title);

        final ListBox box = new ListBox();
        box.addItem("...");
        this.oracle.getFieldCompletions(factType,
                                        FieldAccessorsAndMutators.ACCESSOR,
                                        new Callback<ModelField[]>() {
                                            @Override
                                            public void callback(final ModelField[] fields) {
                                                for (int i = 0; i < fields.length; i++) {
                                                    //You can't use "this" in a nested accessor
                                                    final String fieldName = fields[i].getName();
                                                    if (!isNested || !fieldName.equals(DataType.TYPE_THIS)) {
                                                        box.addItem(fieldName);
                                                    }
                                                }
                                            }
                                        });

        box.setSelectedIndex(0);

        box.addChangeHandler(event -> {
            final String fieldName = box.getItemText(box.getSelectedIndex());

            if ("...".equals(fieldName)) {
                return;
            }

            factPattern.addConstraint(makeFieldConstraint(fieldName,
                                                          factType,
                                                          fieldConstraint));

            modeller.refreshWidget();
            popup.hide();
        });
        popup.addAttribute(GuidedRuleEditorResources.CONSTANTS.AddARestrictionOnAField(),
                           box);

        final ListBox composites = new ListBox();
        composites.addItem("...");
        composites.addItem(GuidedRuleEditorResources.CONSTANTS.AllOfAnd(),
                           CompositeFieldConstraint.COMPOSITE_TYPE_AND);
        composites.addItem(GuidedRuleEditorResources.CONSTANTS.AnyOfOr(),
                           CompositeFieldConstraint.COMPOSITE_TYPE_OR);
        composites.setSelectedIndex(0);

        composites.addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                CompositeFieldConstraint comp = new CompositeFieldConstraint();
                comp.setCompositeJunctionType(composites.getValue(composites.getSelectedIndex()));
                factPattern.addConstraint(comp);
                modeller.refreshWidget();
                popup.hide();
            }
        });

        InfoPopup infoComp = new InfoPopup(GuidedRuleEditorResources.CONSTANTS.MultipleFieldConstraints(),
                                           GuidedRuleEditorResources.CONSTANTS.MultipleConstraintsTip1());

        HorizontalPanel horiz = new HorizontalPanel();

        horiz.add(composites);
        horiz.add(infoComp);
        if (fieldConstraint == null) {
            popup.addAttribute(GuidedRuleEditorResources.CONSTANTS.MultipleFieldConstraint(),
                               horiz);
        }

        if (fieldConstraint == null) {
            popup.addRow(new SmallLabel("<i>" + GuidedRuleEditorResources.CONSTANTS.AdvancedOptionsColon() + "</i>")); //NON-NLS
            Button predicate = new Button(GuidedRuleEditorResources.CONSTANTS.NewFormula());
            predicate.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    SingleFieldConstraint con = new SingleFieldConstraint();
                    con.setConstraintValueType(SingleFieldConstraint.TYPE_PREDICATE);
                    factPattern.addConstraint(con);
                    modeller.refreshWidget();
                    popup.hide();
                }
            });
            popup.addAttribute(GuidedRuleEditorResources.CONSTANTS.AddANewFormulaStyleExpression(),
                               predicate);

            Button ebBtn = new Button(GuidedRuleEditorResources.CONSTANTS.ExpressionEditor());

            ebBtn.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    SingleFieldConstraintEBLeftSide con = new SingleFieldConstraintEBLeftSide();
                    con.setConstraintValueType(SingleFieldConstraint.TYPE_UNDEFINED);
                    factPattern.addConstraint(con);
                    con.setExpressionLeftSide(new ExpressionFormLine(new ExpressionUnboundFact(pattern.getFactType())));
                    modeller.refreshWidget();
                    popup.hide();
                }
            });
            popup.addAttribute(GuidedRuleEditorResources.CONSTANTS.ExpressionEditor(),
                               ebBtn);

            doBindingEditor(popup);
        }

        popup.show();
    }

    SingleFieldConstraint makeFieldConstraint(final String fieldName,
                                              final String factType,
                                              final SingleFieldConstraint parentFieldConstraint) {

        final String fieldType = getDataModelOracle().getFieldType(factType,
                                                                   fieldName);

        if (parentFieldConstraint == null) {
            return new SingleFieldConstraint(factType,
                                             fieldName,
                                             fieldType,
                                             null);
        }

        final SingleFieldConstraintEBLeftSide fieldConstraint = new SingleFieldConstraintEBLeftSide(factType,
                                                                                                    fieldName,
                                                                                                    fieldType,
                                                                                                    parentFieldConstraint);

        fieldConstraint.setExpressionLeftSide(makeLeftSideExpression(fieldName,
                                                                     factType,
                                                                     fieldType,
                                                                     parentFieldConstraint));

        return fieldConstraint;
    }

    ExpressionFormLine makeLeftSideExpression(final String fieldName,
                                              final String factType,
                                              final String fieldType,
                                              final SingleFieldConstraint parentFieldConstraint) {
        final ExpressionField fieldExpression = new ExpressionField(parentFieldConstraint.getFieldName(),
                                                                    factType,
                                                                    fieldType);
        final ExpressionField subFieldExpression = new ExpressionField(fieldName,
                                                                       fieldType,
                                                                       fieldType);
        final ExpressionFormLine expression = new ExpressionFormLine(fieldExpression);

        expression.appendPart(subFieldExpression);

        return expression;
    }

    private String getFactType(FactPattern fp,
                               SingleFieldConstraint sfc) {
        String factType;
        if (sfc == null) {
            factType = fp.getFactType();
        } else {
            factType = sfc.getFieldType();
            //If field name is "this" use parent FactPattern type otherwise we can use the Constraint's field type
            String fieldName = sfc.getFieldName();
            if (DataType.TYPE_THIS.equals(fieldName)) {
                factType = fp.getFactType();
            }
        }
        return factType;
    }

    /**
     * This adds in (optionally) the editor for changing the bound variable
     * name. If its a bindable pattern, it will show the editor, if it is
     * already bound, and the name is used, it should not be editable.
     */
    private void doBindingEditor(final FormStylePopup popup) {
        if (bindable || !(modeller.getModel().isBoundFactUsed(pattern.getBoundName()))) {
            HorizontalPanel varName = new HorizontalPanel();
            final TextBox varTxt = new BindingTextBox();
            if (pattern.getBoundName() == null) {
                varTxt.setText("");
            } else {
                varTxt.setText(pattern.getBoundName());
            }

            ((InputElement) varTxt.getElement().cast()).setSize(6);
            varName.add(varTxt);

            Button bindVar = new Button(HumanReadableConstants.INSTANCE.Set());
            bindVar.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    String var = varTxt.getText();
                    if (modeller.isVariableNameUsed(var)) {
                        Window.alert(GuidedRuleEditorResources.CONSTANTS.TheVariableName0IsAlreadyTaken(var));
                        return;
                    }
                    pattern.setBoundName(varTxt.getText());
                    modeller.refreshWidget();
                    popup.hide();
                }
            });

            varName.add(bindVar);
            popup.addAttribute(GuidedRuleEditorResources.CONSTANTS.VariableName(),
                               varName);
        }
    }
}
