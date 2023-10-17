/*
 * This file is part of Codion Petclinic Demo.
 *
 * Codion Petclinic Demo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Codion Petclinic Demo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Codion Petclinic Demo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (c) 2004 - 2023, Björn Darri Sigurðsson.
 */
package is.codion.framework.demos.petclinic.ui;

import is.codion.framework.demos.petclinic.domain.Petclinic.Pet;
import is.codion.framework.demos.petclinic.domain.Petclinic.PetType;
import is.codion.swing.common.ui.control.Control;
import is.codion.swing.framework.model.SwingEntityEditModel;
import is.codion.swing.framework.ui.EntityEditPanel;
import is.codion.swing.framework.ui.component.EntityComboBox;

import javax.swing.JPanel;

import static is.codion.swing.common.ui.component.button.ButtonPanelBuilder.createEastButtonPanel;
import static is.codion.swing.common.ui.layout.Layouts.gridLayout;

public final class PetEditPanel extends EntityEditPanel {

  public PetEditPanel(SwingEntityEditModel editModel) {
    super(editModel);
  }

  @Override
  protected void initializeUI() {
    initialFocusAttribute().set(Pet.NAME);

    createForeignKeyComboBox(Pet.OWNER_FK);
    createTextField(Pet.NAME);
    createTemporalInputPanel(Pet.BIRTH_DATE);
    EntityComboBox petTypeBox =
            createForeignKeyComboBox(Pet.PET_TYPE_FK)
                    .build();

    Control newPetTypeControl = createInsertControl(petTypeBox, () ->
            new PetTypeEditPanel(new SwingEntityEditModel(PetType.TYPE, editModel().connectionProvider())));
    JPanel petTypePanel = createEastButtonPanel(petTypeBox, newPetTypeControl);

    setLayout(gridLayout(2, 2));

    addInputPanel(Pet.OWNER_FK);
    addInputPanel(Pet.NAME);
    addInputPanel(Pet.BIRTH_DATE);
    addInputPanel(Pet.PET_TYPE_FK, petTypePanel);
  }
}
