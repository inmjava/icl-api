# Template EAP
Este é um template para aplicações a serem implantadas usando a imagem S2I do EAP para OpenShift.
Contém tudo o que é necessário para qualquer aplicação **que já esteja empacotada como EAR/WAR**.

## O que você pode (e deve) alterar
* O conteúdo da pasta _configuration_: instruções no arquivo standalone.xml
* O conteúdo da pasta _deployments_: copie seus arquivos EAR/WAR aqui
* O conteúdo da pasta _fonte_: Adicione suas pastas com o código fonte, juntamente com o arquivo pom.xml (remova o ear da pasta deployment)
* O conteúdo da pasta _lib_: inclua bibliotecas necessárias que devem ser incorporadas ao EAP (não recomendado)
* O conteúdo do arquivo health_check.sh:
	* Remova (ou comente com #) a primeira linha "exit 0"
	* Altere o final da variável URL_TEST adicionando um contexto válido para a aplicação


## O que você **NÃO** deve alterar
* O que estiver nas outras pastas (.sti, bin e modules)
* Caso ache necessário alterar o conteúdo destas pastas, procure a equipe PAAS
