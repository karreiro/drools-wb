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

package org.drools.workbench.screens.guided.dtable.client.wizard.column.pages.common;

import com.google.gwt.user.client.ui.ListBox;

public class DecisionTableColumnViewUtils {

    public static int getCurrentValueIndex( final String currentValue,
                                            final ListBox list ) {

        for ( int index = 0; index < list.getItemCount(); index++ ) {
            final String value = list.getValue( index );

            if ( value.equals( currentValue ) ) {
                return index;
            }
        }

        return 0;
    }

    public static boolean nil( String s ) {
        return s == null || s.equals( "" );
    }
}
