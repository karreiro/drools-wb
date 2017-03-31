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

package org.drools.workbench.screens.guided.dtable.client.wizard.column.commons;

import org.drools.workbench.models.guided.dtable.shared.model.DTColumnConfig52;

public interface HasAdditionalInfoPage {

    DTColumnConfig52 editingCol();

    String getHeader();

    void setHeader(final String header);

    void setInsertLogical(final Boolean value);

    void setUpdate(final Boolean value);

    boolean showUpdateEngineWithChanges();

    boolean showLogicallyInsert();
}
