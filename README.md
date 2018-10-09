# Problema da Montanha Russa
Trabalho da disciplina de Sistemas Operacionais. IFCE - Campus Fortaleza.

## O problema
Suponha que existe um parque, onde há uma montanha russa de apenas um vagão com capacidade N. Neste parque, vários leões (threads) 
desejam repetidamente andar na montanha russa. Deve-se utilizar uma lógica com semáforos, que respeite os seguintes critérios:
* O vagão só parte quando está lotado. 
* Apenas um passageiro deve embarcar ou desembarcar por vez.
* Os passageiros em viagem devem curtir (não dormir) dentro do vagão.
* Os passageiros que estão na fila devem dormir até que o vagão chegue.
* Cada passageiro tem seu tempo de embarque e desembarque.

## Descrição técnica
O problema foi solucionado utilizando uma lógica de semáforos, expressa nos arquivos Pseudo_Codigo_SO_Passageiros.rtf e
Pseudo_Codigo_SO_Vagao.rtf. As animações foram feitas em JavaFX.

## Como testar
Abra o cmd e digite "java -jar C:/users/<usuário>/<diretório da pasta do projeto>/dist/MontanhaRussaFX.jar"
