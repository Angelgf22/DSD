service Calculadora{
   void ping(),
   double suma(1:double num1, 2:double num2),
   double resta(1:double num1, 2:double num2),
   double multiplicacion(1:double num1, 2:double num2),
   double division(1:double num1, 2:double num2),
   list<i32> sumavectores(1:list<i32> v1, 2:list<i32> v2),
   list<list<i32>> sumamatrices(1:list<list<i32>> m1, 2:list<list<i32>> m2),
   list<list<i32>> multiplicacionmatrices(1:list<list<i32>> m1, 2:list<list<i32>> m2),
}
