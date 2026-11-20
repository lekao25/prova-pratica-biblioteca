# Respostas — Sistema de Emprestimo da Biblioteca

Nome completo: Alex Barbosa Dias  RA: 3111392523025

> Preencha **todos** os campos. Campo vazio é campo não avaliado.
> Não apague os títulos nem os rótulos em negrito: a correção procura por eles.

---

# Etapa 1 — Diagnóstico

## Problema 1 — a conversão do acervo central

**Em que consiste o problema, e o que acontece quando o sistema cresce:**
O serviço externo `AcervoLegadoWS` devolve os exemplares como uma matriz de texto (`String[][]`) onde cada linha representa `[codigo, descricao, situacao]` e a situação vem codificada como `"D"` ou `"B"`, podendo aparecer em letra minúscula (como `"d"` no `TOM-2003`)[cite: 1, 2, 10]. A lógica de interpretar essa matriz e converter a letra da situação em disponibilidade (`boolean`) foi duplicada em duas classes diferentes e já divergiu: `CatalogoExemplares` usa `"D".equals(linha[2])` (ignora minúscula), enquanto `RelatorioColecao` usa `"D".equalsIgnoreCase(linha[2])` (aceita minúscula), fazendo o `TOM-2003` constar como indisponível no catálogo e disponível no relatório[cite: 1, 3, 8, 10]. Quando o sistema cresce (novas telas, relatórios, regras de status ou mudanças no formato externo), essa conversão precisará ser replicada ou alterada em vários lugares, aumentando a chance de inconsistências silenciosas e dificultando a manutenção.

**Princípio SOLID violado:**
Viola o **Princípio da Responsabilidade Única (SRP — Single Responsibility Principle)**, pois `CatalogoExemplares` e `RelatorioColecao` acumulam a responsabilidade de traduzir/adaptar um formato externo bruto para o objeto de domínio `Exemplar` além de suas funções de negócio[cite: 3, 8], e também viola o **Princípio Aberto/Fechado (OCP — Open/Closed Principle)** e o **Princípio da Inversão de Dependência (DIP — Dependency Inversion Principle)**, já que mudanças na estrutura de dados externa obrigam a alterar diretamente as classes consumidoras de alto nível[cite: 3, 8].

**Classes afetadas (pelo nome):**
`CatalogoExemplares`, `RelatorioColecao` e `Main` (na montagem das dependências), além de `AcervoLegadoWS` como componente externo não modificável[cite: 1, 3, 7, 8].

**Que outra dependência indevida essas classes têm em relação ao
acervo central, e por que ela é um problema:**
Além de duplicar a conversão, `CatalogoExemplares` e `RelatorioColecao` dependem diretamente da **classe concreta** `AcervoLegadoWS` e conhecem os índices internos da sua matriz (`linha[0]`, `linha[1]`, `linha[2]`)[cite: 1, 3, 8]. Isso cria um acoplamento forte com uma implementação concreta de terceiros: se o serviço legado for substituído por outro provedor, API, banco de dados ou dublê de teste, ou se a ordem das colunas da matriz mudar, ambas as classes precisam ser modificadas e recompiladas.

## Problema 2 — o ciclo de vida de Emprestimo

**Em que consiste o problema, e o que acontece quando o sistema cresce:**
O ciclo de vida de `Emprestimo` (`RESERVADO`, `EMPRESTADO` e `ENCERRADO`) é controlado por um atributo `String situacao` e por estruturas condicionais `if`/`else` espalhadas dentro dos métodos públicos `emprestar()`, `restituir()` e `anular()`. Com isso, ninguém consegue visualizar o ciclo de vida inteiro em um único lugar, o uso de `String` permite erros de digitação que só são descobertos em tempo de execução, e à medida que o sistema cresce (com novos estados, como atrasado ou renovado, ou novas operações), todos os métodos públicos precisam ser editados com novas ramificações condicionais, aumentando o risco de quebrar regras já existentes[cite: 4, 10].

**Princípio SOLID violado:**
Viola o **Princípio Aberto/Fechado (OCP — Open/Closed Principle)**, pois a inclusão de novos estados ou transições exige modificar o código interno dos métodos existentes em `Emprestimo` em vez de estender o comportamento por novas classes[cite: 4], e também o **Princípio da Responsabilidade Única (SRP — Single Responsibility Principle)**, pois `Emprestimo` concentra tanto os dados do empréstimo quanto todas as regras de validação e transição de cada fase[cite: 4].

**Classes afetadas (pelo nome):**
`Emprestimo` (e `Main` apenas como cliente que executa as transições)[cite: 4, 7].

---

# Etapa 4 — Justificativa

## Padrão aplicado ao Problema 1

**O problema que ele resolve (descreva antes de nomear o padrão):**
Ele resolve o problema de ter classes internas do sistema acopladas diretamente a uma classe concreta externa que fornece dados em uma interface e formato incompatíveis (matriz de `String` com códigos de letra sujeitos a variação de caixa), isolando e centralizando a conversão para `List<Exemplar>` em um único componente intermediário e fazendo os consumidores dependerem apenas de uma abstração limpa[cite: 1, 2, 3, 8].

**Nome do padrão:**
**Adapter** (padrão estrutural).

**Alternativa descartada e por que ela não resolveria por completo:**
Descartei **alterar o serviço legado (`AcervoLegadoWS`)** porque o enunciado proíbe modificá-lo e, na prática, ele representa um componente externo mantido por outra equipe fora do controle do projeto[cite: 2, 10]. Além disso, mesmo que a classe externa pudesse ser editada para padronizar a letra maiúscula, isso não eliminaria o acoplamento direto de `CatalogoExemplares` e `RelatorioColecao` com a classe concreta `AcervoLegadoWS` nem com a estrutura posicional da matriz `String[][]`[cite: 1, 2, 3, 8].

## Padrão aplicado ao Problema 2

**O problema que ele resolve (descreva antes de nomear o padrão):**
Ele resolve o problema de espalhar condicionais (`if`/`else`) baseadas em um campo de estado pelos métodos públicos de uma classe, encapsulando o comportamento permitido e as transições válidas de cada fase do ciclo de vida em classes próprias e fazendo a classe principal delegar as operações para o objeto que representa sua fase atual[cite: 4, 10].

**Nome do padrão:**
**State** (padrão comportamental).

**Alternativa descartada e por que ela não resolveria por completo:**
Descartei **trocar a `String` por um `enum` simples** (mantendo condicionais em `Emprestimo`) porque, embora o `enum` elimine erros de digitação em tempo de compilação, as regras de transição continuariam concentradas em blocos `if` ou `switch` dentro dos métodos de `Emprestimo`[cite: 4, 10]. Assim, cada novo estado ainda exigiria alterar os métodos públicos da classe principal em vez de permitir ler e estender cada fase de forma isolada em sua própria classe[cite: 4, 10].