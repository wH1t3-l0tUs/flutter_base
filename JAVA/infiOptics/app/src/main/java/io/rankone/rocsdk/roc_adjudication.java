/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.0
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package io.rankone.rocsdk;

public class roc_adjudication {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  public roc_adjudication(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(roc_adjudication obj) {
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
        rocJNI.delete_roc_adjudication(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setA(long value) {
    rocJNI.roc_adjudication_a_set(swigCPtr, this, value);
  }

  public long getA() {
    return rocJNI.roc_adjudication_a_get(swigCPtr, this);
  }

  public void setB(long value) {
    rocJNI.roc_adjudication_b_set(swigCPtr, this, value);
  }

  public long getB() {
    return rocJNI.roc_adjudication_b_get(swigCPtr, this);
  }

  public void setSame_person(boolean value) {
    rocJNI.roc_adjudication_same_person_set(swigCPtr, this, value);
  }

  public boolean getSame_person() {
    return rocJNI.roc_adjudication_same_person_get(swigCPtr, this);
  }

  public roc_adjudication() {
    this(rocJNI.new_roc_adjudication(), true);
  }

}
