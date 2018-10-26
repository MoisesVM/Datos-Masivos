/* 1- Verifique solo numero Par
- se creara una funcion que al ingresar cualquier numero te indique si este
es par o por otra parte impar*/
def parimpar(num:Int): Boolean =
  {if(num%2==0){
    true}
  else{
    false}
  }
parimpar(24) //se le manda un numero como parametro

/* 2.- Buscar numeros pares en lista
- se crea una funcion que busque entre una lista de numeros 
si hay dentro un numero par*/
val numeros = List (5,7,8,2) // se crea una lista predefinida de numeros
def checkit(num:List[Int]): Boolean =
  { return(num(1)%2==0)
}
println(checkit(numeros)) /* se imprime la lista primero llamando antes a la funcion
y posteriormente pasandole como parametro la lista numerica*/

/* 3.- Afortunado numero 7 
- se creara una funcion que de una lista de numeros calcule la suma, si en ella aparece
un numero 7, este se contara dos veces por lo que la suma sera 14 */
def afornum(list:List[Int]): Int =
{var res=0
  for(n <- list){
    if (n==7){     // aqui se indica la condicion, identificando si se encuentra un 7
      res = res + 14
    } else {
      res = res + n
    }
  }
  return res
}
val nums = List(9,3,5,2,7,7,6) //se crea la lista predefinida
println(afornum(nums)) // se imprimen los datos pasando los parametros

/* 4.- Puedes equilibrar
- Para esta funcion, crearemos una lista predefinida, al igual que variables para
distintas operaciones con ella*/
val lista = List(9,6,3)
var a = lista.length
var b = (a%2)-1
var suma = 0
var x = b + 1
for (x <-x to a){ //se crea un ciclo para la acumulacion de la suma
  suma = suma + lista (x)
}
if (lista(b) == suma){  // se crea un bloque de condicion
  (lista(b) == suma)
}else {
  (lista(b) == suma)
}
println(suma) // se imprimen los datos

/* 5.- Verificar palindromo 
- para esta parte, crearemos una funcion que reciba una cadena y devuelva un boleano
en caso de encontrar un palindromo (Palabra o expresión que es igual 
si se lee de izquierda a derecha que de derecha a izquierda) */
def palindromo(palabra:String) :Boolean =
{
   return (palabra == palabra.reverse) /* se crea la condicion de comparacion 
   para la palabra recibida, comparando la palabra escrita normal con su reverso*/
}
val palabra = "somos"   // se definen palabras para fines de prueba
val palabra2 = "rapar"

println(palindromo(palabra))  // se imprimen los resultados
println(palindromo(palabra2))
