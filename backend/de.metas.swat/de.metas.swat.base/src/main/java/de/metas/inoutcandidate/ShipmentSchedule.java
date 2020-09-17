/*
 * #%L
 * de.metas.swat.base
 * %%
 * Copyright (C) 2020 metas GmbH
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

package de.metas.inoutcandidate;

import de.metas.bpartner.BPartnerContactId;
import de.metas.bpartner.BPartnerId;
import de.metas.bpartner.BPartnerLocationId;
import de.metas.inoutcandidate.exportaudit.APIExportStatus;
import de.metas.order.OrderAndLineId;
import de.metas.organization.OrgId;
import de.metas.product.ProductId;
import de.metas.quantity.Quantity;
import de.metas.shipping.ShipperId;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.adempiere.mm.attributes.AttributeSetInstanceId;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

@Data
@Builder
public class ShipmentSchedule
{
	@NonNull
	private final ShipmentScheduleId id;

	@NonNull
	private final OrgId orgId;

	@NonNull
	private final BPartnerId customerId;

	@NonNull
	private final BPartnerLocationId locationId;

	@Nullable
	private final BPartnerContactId contactId;

	@Nullable
	private final OrderAndLineId orderAndLineId;

	@Nullable
	private final LocalDateTime dateOrdered;

	@NonNull
	private final ProductId productId;

	@NonNull
	private final Quantity quantityToDeliver;

	@NonNull
	private final Quantity orderedQuantity;

	@NonNull
	private final Quantity deliveredQty;

	@Nullable
	private final AttributeSetInstanceId attributeSetInstanceId;

	@NonNull
	private APIExportStatus exportStatus;

	@Nullable
	private ShipperId shipperId;
}