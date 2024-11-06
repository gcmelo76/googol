## Descrição Geral

Este projeto expande a meta anterior, introduzindo um servidor HTTP que permite ao utilizador interagir com a aplicação através de HTML.

## Controlador

O controlador tem 2 endpoints:

1. Uma página onde aparecem as pesquisas.
2. Uma página para visualizar as informações de administração.

## Integração da Primeira Meta com Spring Boot

Para integrar a primeira meta com Spring Boot, no controlador em que é necessário utilizar a gateway para indexar e pesquisar URLs e termos, criamos a ligação RMI e utilizamo-la para comunicar com a gateway desenvolvida na meta anterior.

## Websockets e Página de Administração

Utilizamos WebSockets para fazer uma página de administração em tempo real. É criado um Bean com scope de aplicação, que é responsável por se registrar na gateway para receber por RMI callback os updates da admin board. Quando o método do RMI callback do bean é chamado, ele envia os updates para o WebSocket em que o código JavaScript da página de administração está à escuta. Quando a página recebe uma mensagem, ela é atualizada automaticamente, tornando-se assim uma página de administração em tempo real.

## REST

Para este projeto, foram utilizadas duas APIs REST:

### Hacker News

- Esta API foi utilizada recolher noticias.
- Em seguida, são efetuadas 10 requisições, uma para cada uma das notícias, e verificamos se os termos pesquisados estão presentes no título ou no conteúdo dos resultados das requisições.
- Caso os termos pesquisados apareçam, os URLs dessas notícias são indexados.
- Este código está presente no nosso controlador de pesquisa, pois ele é executado a cada pesquisa que um utilizador faz.

### Games API

- Como segunda API, utilizamos uma API gratuita que devolve vários jogos grátis.
- Recebemos os resultados e mostramos na tela o título do jogo, o link do jogo e uma imagem do jogo.

## Testes

### Pesquisa e Indexação

- Testamos a introdução de URLs novos, URLs já indexados e termos de pesquisa de sites em que sabíamos qual era o resultado esperado.
- Os resultados mostrados foram de encontro ao desejado.

### Página de Administração em Tempo Real

- Abrimos a página de administração num computador e a página de pesquisa noutro computador.
- Verificamos que ao efetuar a pesquisa num dos computadores, a página de administração atualizava automaticamente com o novo termo de pesquisa.

### API dos Jogos

- Verificamos se os jogos estavam a ser listados corretamente.

### API do Hacker News

- Verificamos se os links com termos pesquisados estavam de facto a ser indexados.
- Para isso, visitamos os URLs e verificamos que estava a funcionar conforme esperado.