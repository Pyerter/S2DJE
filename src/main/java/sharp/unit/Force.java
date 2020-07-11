package sharp.unit;

import sharp.utility.KinAnchor;

/**
 * This functional-interface is used to apply forces to {@code KinAnchor}s.
 */
public interface Force {

    /**
     * This method takes in a {@code KinAnchor} and applies the force to the anchor.
     *
     * @param k - k is the {@code KinAnchor} passed in to apply the force on.
     */
    public void apply(KinAnchor k);

}
