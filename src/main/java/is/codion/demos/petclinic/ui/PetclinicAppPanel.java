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
 * Copyright (c) 2004 - 2025, Björn Darri Sigurðsson.
 */
package is.codion.demos.petclinic.ui;

import is.codion.common.model.CancelException;
import is.codion.common.user.User;
import is.codion.demos.petclinic.domain.Petclinic;
import is.codion.demos.petclinic.domain.Petclinic.Owner;
import is.codion.demos.petclinic.domain.Petclinic.Pet;
import is.codion.demos.petclinic.domain.Petclinic.PetType;
import is.codion.demos.petclinic.domain.Petclinic.Specialty;
import is.codion.demos.petclinic.domain.Petclinic.Vet;
import is.codion.demos.petclinic.domain.Petclinic.VetSpecialty;
import is.codion.demos.petclinic.domain.Petclinic.Visit;
import is.codion.demos.petclinic.model.PetclinicAppModel;
import is.codion.demos.petclinic.model.VetSpecialtyEditModel;
import is.codion.plugin.flatlaf.intellij.themes.arc.Arc;
import is.codion.swing.framework.model.SwingEntityModel;
import is.codion.swing.framework.ui.EntityApplicationPanel;
import is.codion.swing.framework.ui.EntityPanel;
import is.codion.swing.framework.ui.ReferentialIntegrityErrorHandling;

import java.util.List;
import java.util.Locale;

public final class PetclinicAppPanel extends EntityApplicationPanel<PetclinicAppModel> {

	public PetclinicAppPanel(PetclinicAppModel appModel) {
		super(appModel);
	}

	@Override
	protected List<EntityPanel> createEntityPanels() {
		SwingEntityModel ownersModel = applicationModel().entityModels().get(Owner.TYPE);
		SwingEntityModel petsModel = ownersModel.detailModels().get(Pet.TYPE);
		SwingEntityModel visitsModel = petsModel.detailModels().get(Visit.TYPE);

		EntityPanel ownersPanel = new EntityPanel(ownersModel,
						new OwnerEditPanel(ownersModel.editModel()));
		EntityPanel petsPanel = new EntityPanel(petsModel,
						new PetEditPanel(petsModel.editModel()));
		EntityPanel visitsPanel = new EntityPanel(visitsModel,
						new VisitEditPanel(visitsModel.editModel()));

		ownersPanel.detailPanels().add(petsPanel);
		petsPanel.detailPanels().add(visitsPanel);

		return List.of(ownersPanel);
	}

	@Override
	protected List<EntityPanel.Builder> createSupportEntityPanelBuilders() {
		EntityPanel.Builder petTypePanelBuilder =
						EntityPanel.builder(PetType.TYPE)
										.editPanel(PetTypeEditPanel.class)
										.caption("Pet types");
		EntityPanel.Builder specialtyPanelBuilder =
						EntityPanel.builder(Specialty.TYPE)
										.editPanel(SpecialtyEditPanel.class)
										.caption("Specialties");

		SwingEntityModel.Builder vetSpecialtyModelBuilder =
						SwingEntityModel.builder(VetSpecialty.TYPE)
										.editModel(VetSpecialtyEditModel.class);
		SwingEntityModel.Builder vetModelBuilder =
						SwingEntityModel.builder(Vet.TYPE)
										.detailModel(vetSpecialtyModelBuilder);

		EntityPanel.Builder vetSpecialtyPanelBuilder =
						EntityPanel.builder(vetSpecialtyModelBuilder)
										.editPanel(VetSpecialtyEditPanel.class)
										.caption("Specialty");
		EntityPanel.Builder vetPanelBuilder =
						EntityPanel.builder(vetModelBuilder)
										.editPanel(VetEditPanel.class)
										.detailPanel(vetSpecialtyPanelBuilder)
										.caption("Vets");

		return List.of(petTypePanelBuilder, specialtyPanelBuilder, vetPanelBuilder);
	}

	public static void main(String[] args) throws CancelException {
		Locale.setDefault(Locale.of("en", "EN"));
		ReferentialIntegrityErrorHandling.REFERENTIAL_INTEGRITY_ERROR_HANDLING
						.set(ReferentialIntegrityErrorHandling.DISPLAY_DEPENDENCIES);
		EntityApplicationPanel.builder(PetclinicAppModel.class, PetclinicAppPanel.class)
						.applicationName("Petclinic")
						.applicationVersion(PetclinicAppModel.VERSION)
						.domainType(Petclinic.DOMAIN)
						.displayStartupDialog(false)
						.defaultLookAndFeel(Arc.class)
						.defaultLoginUser(User.parse("scott:tiger"))
						.start();
	}
}
