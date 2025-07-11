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
package io.trino.plugin.dameng;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import dm.jdbc.driver.DmDriver;
import io.trino.plugin.jdbc.BaseJdbcConfig;
import io.trino.plugin.jdbc.ConnectionFactory;
import io.trino.plugin.jdbc.DriverConnectionFactory;
import io.trino.plugin.jdbc.ForBaseJdbc;
import io.trino.plugin.jdbc.JdbcClient;
import io.trino.plugin.jdbc.credential.CredentialProvider;

import java.sql.SQLException;
import java.util.Properties;

import static io.airlift.configuration.ConfigBinder.configBinder;

public class DamengClientModule
        implements Module
{
    @Override
    public void configure(Binder binder)
    {
        binder.bind(JdbcClient.class).annotatedWith(ForBaseJdbc.class).to(DamengClient.class).in(Scopes.SINGLETON);
        configBinder(binder).bindConfig(DamengConfig.class);
    }

    @Provides
    @Singleton
    @ForBaseJdbc
    public static ConnectionFactory connectionFactory(BaseJdbcConfig config, CredentialProvider credentialProvider, DamengConfig damengConfig)
            throws SQLException
    {
        Properties connectionProperties = new Properties();
        if (damengConfig.getConnectTimeout() != null) {
            connectionProperties.setProperty("connectTimeout", String.valueOf(damengConfig.getConnectTimeout().toMillis()));
        }

        return new DriverConnectionFactory(
                new DmDriver(),
                config.getConnectionUrl(),
                connectionProperties,
                credentialProvider);
    }
}
