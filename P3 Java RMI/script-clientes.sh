if [ $# -eq 0 ]
  then
    echo "Debe introducir como argumento el nombre de Usuario"
    exit 1
fi


sleep 2
echo
echo "Lanzando el primer cliente"
echo
java -cp . -Djava.security.policy=server.policy Cliente localhost $1
sleep 2
