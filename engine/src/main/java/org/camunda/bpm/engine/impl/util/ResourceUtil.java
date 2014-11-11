/* Licensed under the Apache License, Version 2.0 (the "License");
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

package org.camunda.bpm.engine.impl.util;

import java.io.InputStream;
import java.nio.charset.Charset;

import org.camunda.bpm.engine.exception.NotFoundException;
import org.camunda.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.camunda.bpm.engine.impl.persistence.entity.ResourceEntity;

/**
 * @author Sebastian Menski
 */
public final class ResourceUtil {

  /**
   * Parse a camunda:resource attribute and loads the resource depending on the url scheme.
   * Supported URL schemes are <code>classpath://</code> and <code>deployment://</code>.
   * If the scheme is omitted <code>classpath://</code> is assumed.
   *
   * @param resourcePath the path to the resource to load
   * @param deployment the deployment to load resources from
   * @return the resource content as {@link String}
   */
  public static String loadResourceContent(String resourcePath, DeploymentEntity deployment) {
    String[] pathSplit = resourcePath.split("://", 2);

    String resourceType;
    if (pathSplit.length == 1) {
      resourceType = "classpath";
    } else {
      resourceType = pathSplit[0];
    }

    String resourceLocation = pathSplit[pathSplit.length - 1];

    byte[] resourceBytes = null;

    if (resourceType.equals("classpath")) {
      InputStream resourceAsStream = null;
      try {
        resourceAsStream = ReflectUtil.getResourceAsStream(resourceLocation);
        if (resourceAsStream != null) {
          resourceBytes = IoUtil.readInputStream(resourceAsStream, resourcePath);
        }
      } finally {
        IoUtil.closeSilently(resourceAsStream);
      }
    }
    else if (resourceType.equals("deployment")) {
      ResourceEntity resourceEntity = deployment.getResource(resourceLocation);
      if (resourceEntity != null) {
        resourceBytes = resourceEntity.getBytes();
      }
    }

    if (resourceBytes != null) {
      return new String(resourceBytes, Charset.forName("UTF-8"));
    }
    else {
      throw new NotFoundException("Unable to find resource with name " + resourcePath);
    }
  }

}
