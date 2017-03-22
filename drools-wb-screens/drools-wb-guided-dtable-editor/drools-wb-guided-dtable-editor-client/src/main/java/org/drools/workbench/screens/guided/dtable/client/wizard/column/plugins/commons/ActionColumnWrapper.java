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

import org.drools.workbench.models.guided.dtable.shared.model.ActionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.ActionInsertFactCol52;
import org.drools.workbench.models.guided.dtable.shared.model.ActionSetFieldCol52;
import org.drools.workbench.models.guided.dtable.shared.model.DTCellValue52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryActionInsertFactCol52;
import org.drools.workbench.models.guided.dtable.shared.model.LimitedEntryActionSetFieldCol52;
import org.drools.workbench.screens.guided.dtable.client.wizard.column.plugins.ActionSetFactPlugin;

abstract public class ActionColumnWrapper {

    private ActionSetFactPlugin plugin;

    protected ActionColumnWrapper(final ActionSetFactPlugin plugin) {
        this.plugin = plugin;
    }

    private ActionColumnWrapper() {
    }

    public static ActionColumnWrapper emptyColumn() {
        return new ActionColumnWrapper() {
            @Override
            public boolean isInsertLogical() {
                return false;
            }

            @Override
            public void setInsertLogical(final boolean insertLogical) {

            }

            @Override
            public boolean isUpdate() {
                return false;
            }

            @Override
            public void setUpdate(final boolean update) {

            }

            @Override
            public DTCellValue52 getDefaultValue() {
                return null;
            }

            @Override
            public void setDefaultValue(final DTCellValue52 defaultValue) {

            }

            @Override
            public String getBoundName() {
                return "";
            }

            @Override
            public void setBoundName(final String boundName) {

            }

            @Override
            public String getFactField() {
                return "";
            }

            @Override
            public void setFactField(final String factField) {

            }

            @Override
            public String getFactType() {
                return "";
            }

            @Override
            public void setFactType(final String factType) {

            }

            @Override
            public String getHeader() {
                return "";
            }

            @Override
            public void setHeader(final String header) {

            }

            @Override
            public String getType() {
                return "";
            }

            @Override
            public void setType(final String type) {

            }

            @Override
            public String getValueList() {
                return "";
            }

            @Override
            public void setValueList(final String valueList) {

            }

            @Override
            public ActionCol52 getActionCol52() {
                return new ActionCol52();
            }
        };
    }

    abstract public boolean isInsertLogical();

    abstract public void setInsertLogical(final boolean insertLogical);

    abstract public boolean isUpdate();

    abstract public void setUpdate(final boolean update);

    abstract public DTCellValue52 getDefaultValue();

    abstract public void setDefaultValue(final DTCellValue52 defaultValue);

    abstract public String getBoundName();

    abstract public void setBoundName(final String boundName);

    abstract public String getFactField();

    abstract public void setFactField(final String factField);

    abstract public String getFactType();

    abstract public void setFactType(final String factType);

    abstract public String getHeader();

    abstract public void setHeader(final String header);

    abstract public String getType();

    abstract public void setType(final String type);

    abstract public String getValueList();

    abstract public void setValueList(final String valueList);

    abstract public ActionCol52 getActionCol52();

    protected ActionSetFactPlugin getPlugin() {
        return plugin;
    }

    protected GuidedDecisionTable52.TableFormat tableFormat() {
        return plugin.tableFormat();
    }

    public static class ActionInsertFact extends ActionColumnWrapper {

        final ActionInsertFactCol52 actionCol52;

        public ActionInsertFact(final ActionSetFactPlugin plugin) {
            super(plugin);

            actionCol52 = newActionInsertFact();
        }

        private ActionInsertFactCol52 newActionInsertFact() {

            switch (tableFormat()) {
                case EXTENDED_ENTRY:
                    return new ActionInsertFactCol52();
                case LIMITED_ENTRY:
                    return new LimitedEntryActionInsertFactCol52();
                default:
                    throw new UnsupportedOperationException("Unsupported table format: " + tableFormat());
            }
        }

        @Override
        public boolean isInsertLogical() {
            return actionCol52.isInsertLogical();
        }

        @Override
        public void setInsertLogical(final boolean insertLogical) {
            actionCol52.setInsertLogical(insertLogical);
        }

        @Override
        public boolean isUpdate() {
            return false;
        }

        @Override
        public void setUpdate(boolean update) {
            // empty
        }

        @Override
        public DTCellValue52 getDefaultValue() {
            return actionCol52.getDefaultValue();
        }

        @Override
        public void setDefaultValue(final DTCellValue52 defaultValue) {
            actionCol52.setDefaultValue(defaultValue);
        }

        @Override
        public String getBoundName() {
            return actionCol52.getBoundName();
        }

        @Override
        public void setBoundName(final String boundName) {
            actionCol52.setBoundName(boundName);
        }

        @Override
        public String getFactField() {
            return actionCol52.getFactField();
        }

        @Override
        public void setFactField(final String factField) {
            actionCol52.setFactField(factField);
        }

        @Override
        public String getFactType() {
            return actionCol52.getFactType();
        }

        @Override
        public void setFactType(final String factType) {
            actionCol52.setFactType(factType);
        }

        @Override
        public String getHeader() {
            return actionCol52.getHeader();
        }

        @Override
        public void setHeader(final String header) {
            actionCol52.setHeader(header);
        }

        @Override
        public String getType() {
            return actionCol52.getType();
        }

        @Override
        public void setType(final String type) {
            actionCol52.setType(type);
        }

        @Override
        public String getValueList() {
            return actionCol52.getValueList();
        }

        @Override
        public void setValueList(final String valueList) {
            actionCol52.setValueList(valueList);
        }

        @Override
        public ActionCol52 getActionCol52() {
            return actionCol52;
        }
    }

    public static class ActionSetField extends ActionColumnWrapper {

        private final ActionSetFieldCol52 actionCol52;

        public ActionSetField(final ActionSetFactPlugin plugin) {
            super(plugin);

            this.actionCol52 = newActionSetField();
        }

        private ActionSetFieldCol52 newActionSetField() {

            switch (tableFormat()) {
                case EXTENDED_ENTRY:
                    return new ActionSetFieldCol52();
                case LIMITED_ENTRY:
                    return new LimitedEntryActionSetFieldCol52();
                default:
                    throw new UnsupportedOperationException("Unsupported table format: " + tableFormat());
            }
        }

        @Override
        public boolean isInsertLogical() {
            return false;
        }

        @Override
        public void setInsertLogical(final boolean insertLogical) {
            // empty
        }

        @Override
        public boolean isUpdate() {
            return actionCol52.isUpdate();
        }

        @Override
        public void setUpdate(final boolean update) {
            actionCol52.setUpdate(update);
        }

        @Override
        public DTCellValue52 getDefaultValue() {
            return actionCol52.getDefaultValue();
        }

        @Override
        public void setDefaultValue(final DTCellValue52 defaultValue) {
            actionCol52.setDefaultValue(defaultValue);
        }

        @Override
        public String getBoundName() {
            return actionCol52.getBoundName();
        }

        @Override
        public void setBoundName(final String boundName) {
            actionCol52.setBoundName(boundName);
        }

        @Override
        public String getFactField() {
            return actionCol52.getFactField();
        }

        @Override
        public void setFactField(String factField) {
            actionCol52.setFactField(factField);
        }

        @Override
        public String getFactType() {
            return "";
        }

        @Override
        public void setFactType(final String factType) {
            // empty
        }

        @Override
        public String getHeader() {
            return actionCol52.getHeader();
        }

        @Override
        public void setHeader(String header) {
            actionCol52.setHeader(header);
        }

        @Override
        public String getType() {
            return actionCol52.getType();
        }

        @Override
        public void setType(final String type) {
            actionCol52.setType(type);
        }

        @Override
        public String getValueList() {
            return actionCol52.getValueList();
        }

        @Override
        public void setValueList(final String valueList) {
            actionCol52.setValueList(valueList);
        }

        @Override
        public ActionCol52 getActionCol52() {
            return actionCol52;
        }
    }
}
