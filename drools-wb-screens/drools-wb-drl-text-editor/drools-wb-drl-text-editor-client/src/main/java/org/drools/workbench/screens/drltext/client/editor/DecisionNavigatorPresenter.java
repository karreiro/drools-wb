/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
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

package org.drools.workbench.screens.drltext.client.editor;

import javax.inject.Inject;

import org.uberfire.client.annotations.DefaultPosition;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.mvp.UberElemental;
import org.uberfire.lifecycle.OnStartup;
import org.uberfire.mvp.PlaceRequest;
import org.uberfire.workbench.model.CompassPosition;
import org.uberfire.workbench.model.Position;

@WorkbenchScreen(identifier = "org.kie.dmn.decision.navigator")
public class DecisionNavigatorPresenter {

    @Inject
    private UberElemental<DecisionNavigatorPresenter> view;

    @OnStartup
    public void onStartup(final PlaceRequest placeRequest) {
    }

    @WorkbenchPartView
    public UberElemental<DecisionNavigatorPresenter> getView() {
        return view;
    }

    @WorkbenchPartTitle
    public String getTitle() {
        return "Decision Navigator";
    }

    @DefaultPosition
    public Position getDefaultPosition() {
        return CompassPosition.WEST;
    }

    public interface DecisionNavigatorView extends UberElemental<DecisionNavigatorPresenter> {

    }
}
