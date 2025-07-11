/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.trino.plugin.oceanbase;

import com.google.common.collect.ImmutableList;
import io.trino.plugin.base.session.SessionPropertiesProvider;
import io.trino.plugin.oceanbase.OceanBaseConfig.ArrayMapping;
import io.trino.spi.connector.ConnectorSession;
import io.trino.spi.session.PropertyMetadata;

import javax.inject.Inject;

import java.util.List;

import static io.trino.spi.session.PropertyMetadata.enumProperty;

public final class OceanBaseSessionProperties
        implements SessionPropertiesProvider
{
    public static final String ARRAY_MAPPING = "array_mapping";

    private final List<PropertyMetadata<?>> sessionProperties;

    @Inject
    public OceanBaseSessionProperties(OceanBaseConfig oceanbaseConfig)
    {
        sessionProperties = ImmutableList.of(
                enumProperty(
                        ARRAY_MAPPING,
                        "Handling of oceanbaseConfig arrays",
                        ArrayMapping.class,
                        oceanbaseConfig.getArrayMapping(),
                        false));
    }

    @Override
    public List<PropertyMetadata<?>> getSessionProperties()
    {
        return sessionProperties;
    }

    public static ArrayMapping getArrayMapping(ConnectorSession session)
    {
        return session.getProperty(ARRAY_MAPPING, ArrayMapping.class);
    }
}
