/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.0
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package io.rankone.rocsdk;

public class roc_candidate {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  public roc_candidate(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(roc_candidate obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  @SuppressWarnings("deprecation")
  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        rocJNI.delete_roc_candidate(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setIndex(long value) {
    rocJNI.roc_candidate_index_set(swigCPtr, this, value);
  }

  public long getIndex() {
    return rocJNI.roc_candidate_index_get(swigCPtr, this);
  }

  public void setSimilarity(float value) {
    rocJNI.roc_candidate_similarity_set(swigCPtr, this, value);
  }

  public float getSimilarity() {
    return rocJNI.roc_candidate_similarity_get(swigCPtr, this);
  }

  public roc_candidate() {
    this(rocJNI.new_roc_candidate(), true);
  }

}
