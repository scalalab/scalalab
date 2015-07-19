
// gcc -lgsl  -lgslcblas -fPIC -fpermissive -I"/home/sterg/jdk1.8.0_20/include" -I"/home/sterg/jdk1.8.0_20/include/linux" -shared -o  libGSLOps.so  GSLOps.cpp 

#include <jni.h>
/* Header for class GSLOps_GSLOps */

#include "GSLJava_GSLOps.h"
#include <gsl/gsl_sf_bessel.h>
/*
#include <gsl/gsl_math.h>
#include <gsl/gsl_eigen.h>
*/
extern "C" {
/*
 * Class:     GSLOps_GSLOps
 * Method:    gslsfbesselJ0
 * Signature: (D)D
 */
JNIEXPORT jdouble JNICALL Java_GSLJava_GSLOps_gslSfBesselJ0
  (JNIEnv *env, jobject obj, jdouble  x)
 {

  double y = gsl_sf_bessel_J0(x);	
 return y;
}

}
/*
JNIEXPORT jdouble JNICALL Java_GSLJava_GSLOps_gslEigSymm
  (JNIEnv *env, jobject obj, jdoubleArray da, jint n)
 {
   jdouble *data = (double *)env->GetDoubleArrayElements( da, NULL);

  gsl_matrix_view m = gsl_matrix_view_array(data, n, n);

 gsl_vector *eval = gsl_vector_alloc(n);	
 gsl_matrix *evec = gsl_matrix_alloc(n, n);

gsl_eigen_symmv_workspace * w = gsl_eigen_symmv_alloc(n);

gsl_eigen_symmv( &m.matrix, eval, evec, w);

gsl_eigen_symmv_free(w);

gsl_eigen_symmv_sort(eval, evec, 
   GSL_EIGEN_SORT_ABS_ASC);

  
  int i;
  for (i=0; i < n; i++)
  {
  double eval_i = gsl_vector_get( eval, i);
  gsl_vector_view evec_i = gsl_matrix_column(evec, i);

        printf ("eigenvalue = %g\n", eval_i);
        printf ("eigenvector = \n");
        gsl_vector_fprintf (stdout, 
                            &evec_i.vector, "%g");
      }
 

  gsl_vector_free (eval);
  gsl_matrix_free (evec);

  env->ReleaseDoubleArrayElements( da, data, 0);

 return 1;

}
*/
