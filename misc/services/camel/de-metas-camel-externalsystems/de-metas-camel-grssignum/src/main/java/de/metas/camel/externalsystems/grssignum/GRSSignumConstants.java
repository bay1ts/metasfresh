/*
 * #%L
 * de-metas-camel-grssignum
 * %%
 * Copyright (C) 2021 metas GmbH
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

package de.metas.camel.externalsystems.grssignum;

public interface GRSSignumConstants
{
	String GRSSIGNUM_SYSTEM_NAME = "GRSSignum";

	String JSON_PROPERTY_FLAG = "FLAG";
	String DEFAULT_UOM_CODE = "KGM";

	String ROUTE_PROPERTY_PUSH_BOMs_CONTEXT = "PushBOMsRouteContext";

	String ROUTE_PROPERTY_EXPORT_BPARTNER_CONTEXT = "ExportBPartnerRouteContext";

	String EXPORT_BPARTNER_RETRY_COUNT = "export.bpartner.retry.count";

	String EXPORT_BPARTNER_RETRY_DELAY = "export.bpartner.retry.delay.ms";
}
