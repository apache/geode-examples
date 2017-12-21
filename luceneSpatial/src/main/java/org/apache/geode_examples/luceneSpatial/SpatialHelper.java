/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.geode_examples.luceneSpatial;

import static org.locationtech.spatial4j.distance.DistanceUtils.EARTH_MEAN_RADIUS_MI;

import org.apache.lucene.document.Field;
import org.apache.lucene.search.Query;
import org.apache.lucene.spatial.query.SpatialArgs;
import org.apache.lucene.spatial.query.SpatialOperation;
import org.apache.lucene.spatial.vector.PointVectorStrategy;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.distance.DistanceUtils;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.impl.GeoCircle;
import org.locationtech.spatial4j.shape.impl.PointImpl;

public class SpatialHelper {
  private static final SpatialContext CONTEXT = SpatialContext.GEO;
  private static final PointVectorStrategy STRATEGY =
      new PointVectorStrategy(CONTEXT, "location", PointVectorStrategy.DEFAULT_FIELDTYPE);

  /**
   * Return a lucene query that finds all points within the given radius from the given point
   */
  public static Query findWithin(double longitude, double latitude, double radiusMiles) {
    // Covert the radius in miles to a radius in degrees
    double radiusDEG = DistanceUtils.dist2Degrees(radiusMiles, EARTH_MEAN_RADIUS_MI);

    // Create a query that looks for all points within a circle around the given point
    SpatialArgs args = new SpatialArgs(SpatialOperation.IsWithin,
        new GeoCircle(createPoint(longitude, latitude), radiusDEG, CONTEXT));
    return STRATEGY.makeQuery(args);
  }

  /**
   * Return a list of fields that should be added to lucene document to index the given point
   */
  public static Field[] getIndexableFields(double longitude, double latitude) {
    Point point = createPoint(longitude, latitude);
    return STRATEGY.createIndexableFields(point);
  }

  private static Point createPoint(double longitude, double latitude) {
    return new PointImpl(longitude, latitude, CONTEXT);
  }

}
