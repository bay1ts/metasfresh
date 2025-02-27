package de.metas.material.dispo.service.event.handler.pporder;

import com.google.common.collect.ImmutableList;
import de.metas.Profiles;
import de.metas.material.cockpit.view.MainDataRecordIdentifier;
import de.metas.material.cockpit.view.mainrecord.MainDataRequestHandler;
import de.metas.material.cockpit.view.mainrecord.UpdateMainDataRequest;
import de.metas.material.dispo.commons.candidate.Candidate;
import de.metas.material.dispo.commons.candidate.CandidateBusinessCase;
import de.metas.material.dispo.commons.candidate.CandidateType;
import de.metas.material.dispo.commons.candidate.businesscase.Flag;
import de.metas.material.dispo.commons.candidate.businesscase.ProductionDetail;
import de.metas.material.dispo.service.candidatechange.CandidateChangeService;
import de.metas.material.dispo.service.candidatechange.handler.CandidateHandler;
import de.metas.material.event.MaterialEventHandler;
import de.metas.material.event.commons.MaterialDescriptor;
import de.metas.material.event.pporder.MaterialDispoGroupId;
import de.metas.material.event.pporder.PPOrder;
import de.metas.material.event.pporder.PPOrderCreatedEvent;
import de.metas.organization.IOrgDAO;
import de.metas.util.Services;
import lombok.NonNull;
import org.compiere.util.TimeUtil;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Collection;

/*
 * #%L
 * metasfresh-material-dispo
 * %%
 * Copyright (C) 2017 metas GmbH
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

@Service
@Profile(Profiles.PROFILE_MaterialDispo)
public final class PPOrderCreatedHandler
		implements MaterialEventHandler<PPOrderCreatedEvent>
{
	private final CandidateChangeService candidateChangeService;
	private final MainDataRequestHandler mainDataRequestHandler;

	private final IOrgDAO orgDAO = Services.get(IOrgDAO.class);

	public PPOrderCreatedHandler(
			@NonNull final CandidateChangeService candidateChangeService,
			@NonNull final MainDataRequestHandler mainDataRequestHandler)
	{
		this.candidateChangeService = candidateChangeService;
		this.mainDataRequestHandler = mainDataRequestHandler;

	}

	@Override
	public Collection<Class<? extends PPOrderCreatedEvent>> getHandledEventType()
	{
		return ImmutableList.of(PPOrderCreatedEvent.class);
	}

	@Override
	public void validateEvent(@NonNull final PPOrderCreatedEvent event)
	{
		event.validate();
	}

	@Override
	public void handleEvent(@NonNull final PPOrderCreatedEvent event)
	{
		handlePPOrderCreatedEvent(event);
	}

	private MaterialDispoGroupId handlePPOrderCreatedEvent(@NonNull final PPOrderCreatedEvent ppOrderEvent)
	{
		final Candidate headerCandidate = createHeaderCandidate(ppOrderEvent);

		updateMainData(ppOrderEvent);

		return headerCandidate.getGroupId();
	}

	private void updateMainData(final @NonNull PPOrderCreatedEvent ppOrderEvent)
	{
		final ZoneId orgZoneId = orgDAO.getTimeZone(ppOrderEvent.getEventDescriptor().getOrgId());

		final MainDataRecordIdentifier mainDataRecordIdentifier = MainDataRecordIdentifier.builder()
				.warehouseId(ppOrderEvent.getPpOrder().getPpOrderData().getWarehouseId())
				.productDescriptor(ppOrderEvent.getPpOrder().getPpOrderData().getProductDescriptor())
				.date(TimeUtil.getDay(ppOrderEvent.getPpOrder().getPpOrderData().getDatePromised(), orgZoneId))
				.build();

		final UpdateMainDataRequest updateMainDataRequest = UpdateMainDataRequest.builder()
				.identifier(mainDataRecordIdentifier)
				.qtySupplyPPOrder(ppOrderEvent.getPpOrder().getPpOrderData().getQtyOpen())
				.build();

		mainDataRequestHandler.handleDataUpdateRequest(updateMainDataRequest);
	}

	@NonNull
	private Candidate createHeaderCandidate(@NonNull final PPOrderCreatedEvent ppOrderEvent)
	{
		final PPOrder ppOrder = ppOrderEvent.getPpOrder();

		final Candidate.CandidateBuilder builder = Candidate.builderForClientAndOrgId(ppOrder.getPpOrderData().getClientAndOrgId());

		final Candidate headerCandidate = builder
				.type(CandidateType.SUPPLY)
				.businessCase(CandidateBusinessCase.PRODUCTION)
				.businessCaseDetail(createProductionDetailForPPOrder(ppOrderEvent))
				.materialDescriptor(createMaterialDescriptorForPPOrder(ppOrder))
				// .groupId(null) // will be set after save
				.build();

		return candidateChangeService.onCandidateNewOrChange(
				headerCandidate,
				CandidateHandler.OnNewOrChangeAdvise.attemptUpdate(false));
	}

	@NonNull
	private MaterialDescriptor createMaterialDescriptorForPPOrder(@NonNull final PPOrder ppOrder)
	{
		return MaterialDescriptor.builder()
				.date(ppOrder.getPpOrderData().getDatePromised())
				.productDescriptor(ppOrder.getPpOrderData().getProductDescriptor())
				.quantity(ppOrder.getPpOrderData().getQtyOpen())
				.warehouseId(ppOrder.getPpOrderData().getWarehouseId())
				.build();
	}

	@NonNull
	private ProductionDetail createProductionDetailForPPOrder(
			@NonNull final PPOrderCreatedEvent ppOrderEvent)
	{
		final PPOrder ppOrder = ppOrderEvent.getPpOrder();

		return ProductionDetail.builder()
				.advised(extractIsAdviseEvent())
				.pickDirectlyIfFeasible(extractPickDirectlyFlag())
				.qty(ppOrder.getPpOrderData().getQtyRequired())
				.plantId(ppOrder.getPpOrderData().getPlantId())
				.productPlanningId(ppOrder.getPpOrderData().getProductPlanningId())
				.ppOrderId(ppOrder.getPpOrderId())
				.ppOrderDocStatus(ppOrder.getDocStatus())
				.build();
	}

	private Flag extractIsAdviseEvent()
	{
		return Flag.FALSE_DONT_UPDATE;
	}

	private Flag extractPickDirectlyFlag()
	{
		return Flag.FALSE_DONT_UPDATE;
	}
}
