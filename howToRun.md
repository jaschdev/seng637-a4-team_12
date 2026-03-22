## Run JUnit Tests Separately

From the project root: /home/bear/winterMeng/637Dependability/assignments/A4/Jfree/Assignment3-artifacts/jfreechart-1.0.19
mvn -Dtest=org.jfree.data.RangeTest test
mvn -Dtest=org.jfree.data.DataUtilitiesTest test

PIT:

mvn org.pitest:pitest-maven:mutationCoverage \
  -DtargetClasses=org.jfree.data.Range \
  -DtargetTests=org.jfree.data.RangeTest


  mvn org.pitest:pitest-maven:mutationCoverage \
  -DtargetClasses=org.jfree.data.DataUtilities \
  -DtargetTests=org.jfree.data.DataUtilitiesTest