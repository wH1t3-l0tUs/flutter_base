/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.0
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package io.rankone.rocsdk;

public class roc_embedded_glasses {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  public roc_embedded_glasses(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(roc_embedded_glasses obj) {
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
        rocJNI.delete_roc_embedded_glasses(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setNone(float value) {
    rocJNI.roc_embedded_glasses_none_set(swigCPtr, this, value);
  }

  public float getNone() {
    return rocJNI.roc_embedded_glasses_none_get(swigCPtr, this);
  }

  public void setSun(float value) {
    rocJNI.roc_embedded_glasses_sun_set(swigCPtr, this, value);
  }

  public float getSun() {
    return rocJNI.roc_embedded_glasses_sun_get(swigCPtr, this);
  }

  public void setEye(float value) {
    rocJNI.roc_embedded_glasses_eye_set(swigCPtr, this, value);
  }

  public float getEye() {
    return rocJNI.roc_embedded_glasses_eye_get(swigCPtr, this);
  }

  public roc_embedded_glasses() {
    this(rocJNI.new_roc_embedded_glasses(), true);
  }

}
