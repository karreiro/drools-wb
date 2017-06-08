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

package org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.commons;

import java.util.List;

import org.drools.workbench.models.guided.dtable.shared.model.ActionWorkItemInsertFactCol52;
import org.drools.workbench.models.guided.dtable.shared.model.ActionWorkItemSetFieldCol52;
import org.drools.workbench.models.guided.dtable.shared.model.BaseColumn;
import org.drools.workbench.models.guided.dtable.shared.model.BaseColumnFieldDiff;

public class ActionWorkItemInsertWrapper extends ActionInsertFactWrapper implements ActionWorkItemWrapper {

    private ActionWorkItemInsertFactCol52 action52;

    public ActionWorkItemInsertWrapper(final BaseDecisionTableColumnPlugin plugin,
                                       final ActionWorkItemInsertFactCol52 actionCol52) {
        super(plugin);

        action52 = clone(actionCol52);
    }

    public ActionWorkItemInsertWrapper(final BaseDecisionTableColumnPlugin plugin) {
        super(plugin);

        action52 = new ActionWorkItemInsertFactCol52();
    }

    @Override
    public List<BaseColumnFieldDiff> diff(final BaseColumn otherColumn) {
        return getActionCol52().diff(otherColumn);
    }

    @Override
    public String getWorkItemName() {
        return getActionCol52().getWorkItemName();
    }

    @Override
    public void setWorkItemName(final String workItemName) {
        getActionCol52().setWorkItemName(workItemName);
    }

    @Override
    public String getWorkItemResultParameterName() {
        return getActionCol52().getWorkItemResultParameterName();
    }

    @Override
    public void setWorkItemResultParameterName(final String workItemResultParameterName) {
        getActionCol52().setWorkItemResultParameterName(workItemResultParameterName);
    }

    @Override
    public String getParameterClassName() {
        return getActionCol52().getParameterClassName();
    }

    @Override
    public void setParameterClassName(final String parameterClassName) {
        getActionCol52().setParameterClassName(parameterClassName);
    }

    @Override
    public void setFactType(final String factType) {
        getActionCol52().setFactType(factType);
    }

    @Override
    public ActionWorkItemInsertFactCol52 getActionCol52() {
        return action52;
    }

    private ActionWorkItemInsertFactCol52 clone(final ActionWorkItemInsertFactCol52 column) {
        final ActionWorkItemInsertFactCol52 clone = new ActionWorkItemInsertFactCol52();

        clone.setFactField(column.getFactField());
        clone.setBoundName(column.getBoundName());
        clone.setValueList(column.getValueList());
        clone.setHeader(column.getHeader());
        clone.setInsertLogical(column.isInsertLogical());
        clone.setDefaultValue(column.getDefaultValue());
        clone.setFactType(column.getFactType());
        clone.setHideColumn(column.isHideColumn());
        clone.setType(column.getType());
        clone.setParameterClassName(column.getParameterClassName());
        clone.setWorkItemName(column.getWorkItemName());
        clone.setWorkItemResultParameterName(column.getWorkItemResultParameterName());

        return clone;
    }
}
