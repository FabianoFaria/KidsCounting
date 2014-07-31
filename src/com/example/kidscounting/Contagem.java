package com.example.kidscounting;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Contagem extends Activity {
	
	private static final String TAG = "Contagem de objetos activity";
	
	private List<String> fileList; //Nomes dos arquivos de figuras...
	private List<String> quizListaFiguras; //lista das figuras para o teste...
	
	private OnClickListener guessButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v)
		{
			submitGuess((Button) v);
		}
	};
	
	private Handler handler;
	private Random random;
	
	private TextView contadorQuestao;
	private TextView ajuda;
	private TextView txtResposta;
	private TableLayout buttonTableLayout;
	private Button btnSair;
	private ImageView imagemObjetos;
	private String nomeDaFigura;
	
	
	private int totalTentativas; // Total de tentativas...
	private int respostaCorreta; //resposta de cada questão
	private int totalRespostasCorretas; //Total de resposta certas...
	private int rowNunbers;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contagem);
		
		contadorQuestao = (TextView)findViewById(R.id.textContador);
		ajuda = (TextView)findViewById(R.id.textAjuda);
		buttonTableLayout =(TableLayout)findViewById(R.id.TableBtnLayoutContar);
		txtResposta = (TextView)findViewById(R.id.textRespostaDesenho);
		btnSair = (Button) findViewById(R.id.btnSair);
		imagemObjetos = (ImageView) findViewById(R.id.imgNumeros);
		
		
		
		fileList = new ArrayList<String>();
		quizListaFiguras = new ArrayList<String>();
		
		rowNunbers = 1;
		random = new Random();
		handler = new Handler();
		
		
		ajuda.setText("Conte os objetos e clique no numero correto");
		txtResposta.setText(" ");
		
		resetPergunta();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contagem, menu);
		return true;
	}
	
	//fecha a activity...
	public void retorno(View view){
		finish();
	}
	
	
	private void resetPergunta(){
		
		//Usa o componente asset manager para obter os nomes dos arquivos de imagem de bandeira 
		AssetManager assets = getAssets();
		
		fileList.clear();
		
		try {
			
			String[] paths = assets.list("");
			
			
	        for (String path : paths)
	        	fileList.add(path.replace(".png", " "));
		} catch (IOException e){
			Log.e(TAG, "Error loading image file names", e);
		}
		
		//Adicionar 10 nomes de arquivos aleatorios para o exercicio...
		
		int contaImagem = 1;
		int numeroDeImagens = fileList.size(); //Obtem o numero de imagens
		
		while (contaImagem <=4) {
			
			int randomIndex = random.nextInt(numeroDeImagens);
			
			//Obtem o nome do arquivo aleatorio...
			
			String fileName = fileList.get(randomIndex);
			
			quizListaFiguras.add(fileName);
			++contaImagem;
			
		}
		
		totalTentativas = 0;
		totalRespostasCorretas = 0;
		txtResposta.setText(" ");
		contadorQuestao.setText(" ");
		carregaPergunta();
	}
	
	
private void carregaPergunta(){
	
		//obtem o nome do arquivo da proxima bandeira e o remove da lista...
	
		String proximaFigura = quizListaFiguras.remove(0);
		nomeDaFigura = proximaFigura; //atualiza a resposta correta...
		txtResposta.setText(" "); // Limpa o TextView da activity
		
		
		
		AssetManager assets = getAssets();
		
		InputStream stream;
		
		
		try{
			
			stream = assets.open(proximaFigura); // retirado o ( "/" +proximaFigura + ".jpg");  devido a erro para abrir o arquivo...
			
			Drawable imagem = Drawable.createFromStream(stream, proximaFigura);
			
			imagemObjetos.setImageDrawable(imagem);
		} //fim do try...
			catch(IOException e) {
				Log.e(TAG, "Error loading " + proximaFigura, e);
			}
		
		
		Collections.shuffle(fileList);
		
		int nomeCorreto = fileList.indexOf(proximaFigura);
		fileList.add(fileList.remove(nomeCorreto));
		
		
		//Codigo para Inflar o layout...
		
			//Limpar os botões existentes
		
			for(int row = 0; row < buttonTableLayout.getChildCount(); ++row)
				((TableRow) buttonTableLayout.getChildAt(row)).removeAllViews();
			
			//Obter a referencia para o LayoutInflater...
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			for (int row = 0; row < rowNunbers; row++ ){
				
				TableRow currentTableRow = getTableRow(row);
				
				// coloca os componentes Buttom em currentTableRow
				for(int column = 0; column < 4; column++){ //Tentativa de colocar 4 botões na activity...
					//infla o guessguess_buttom.xml...
					Button newguessButtom = (Button) inflater.inflate(R.layout.guess_button, null);
					
					//Settar o texto dos botôes...
					String nomeDoArquivo = fileList.get((row * 4) + column);
					newguessButtom.setText(getObjectName(nomeDoArquivo));
					
					//Registra answerButtomListener
					newguessButtom.setOnClickListener(guessButtonListener);
					currentTableRow.addView(newguessButtom);
					
					//Substituir aleatoriamente um componente Button pela resposta correta...
			
				} //fim do segundo primeiro for
				
			} //fim do primeir for
		int row = random.nextInt(rowNunbers); //Escolhe a linha aleatoriamente....
		int column = random.nextInt(3); // Ira escolher uma linha aleatoria para inserir a resposta;
		TableRow randomTableRow = getTableRow(row);
		String nomeResposta = String.valueOf(respostaCorreta);
		((Button)randomTableRow.getChildAt(column)).setText(nomeResposta);
	}//Fim do metodo
	
	
	private TableRow getTableRow(int row){
		return (TableRow) buttonTableLayout.getChildAt(row);
	}
	
	//funação para corrigir o nome do objeto antres de exibilo...
	
	private String getObjectName(String name){
		return name.substring(name.indexOf('-') + 1).replace('_', ' ');
	}
	
	
	
	//Processa a resposta do usuario...
	private void submitGuess( Button guessButton){
	
		String guess = guessButton.getText().toString();
		String respostaQuestão = String.valueOf(nomeDaFigura);
		System.out.println(respostaQuestão);
		++totalTentativas;
		
		//Se a resposta estiver correta...
		
				if(guess.equals(respostaQuestão)){
					
					++totalRespostasCorretas;
					
					
					System.out.println(totalRespostasCorretas);
					System.out.println(totalTentativas);
					//Exibe correto em texto verde...
					
					contadorQuestao.setText(totalRespostasCorretas + " de 10");
					
					txtResposta.setText("= "+ respostaQuestão + " !" );
					txtResposta.setTextColor(getResources().getColor(R.color.resposta_correta));
					
					//Desabilita os demais botões...
					
					disableButtons();
					
					//Se o usuario respondeu as 10 perguntas corretamente...
					
					if(totalRespostasCorretas == 10){
						
						//Cria um novo componente AlertDialog Builder...
						AlertDialog.Builder builder = new AlertDialog.Builder(this);
						
						builder.setTitle(R.string.resultadoTotal); //String da barra de titulo..
						
						// configura a mensagem de alertDialog para exibir o resultado do jogo...
						builder.setMessage(String.format("%d %s, %.02f%% %s", 
								totalRespostasCorretas, getResources().getString(R.string.guesses), 
								(1000 / (double) totalTentativas),
								getResources().getString(R.string.correta)));
						
						builder.setCancelable(false);
						
						//Adicionar o botão para resetar o teste...
						
						builder.setPositiveButton(R.string.reset,
								new DialogInterface.OnClickListener() {
									
									public void onClick(DialogInterface arg0, int arg1) {
										// TODO Auto-generated method stub
										resetPergunta();
									}//fim do metodo Onclick...
								} // fim da classe interna anonima...
						); //fim da chamada de stePositive Button...
						
						AlertDialog resetDialog = builder.create();
						resetDialog.show();  // exibe o componente Dialog
						
					} //fim do if...
					else // a resposta está certa. mas o teste não acabou...
					{
						//carrega a proxima pergunta apos um atraso de 1seg...
						handler.postDelayed( 
								new Runnable(){
									
									@Override
									public void run(){
										carregaPergunta();
									}
								}, 1000); // 1000 milisegundos para atraso de 1 segundo
					} // fim do 
				}
				else // o palpite foi incorreto..
				{
					//exibe a animação...
					
					
					//Exibe "Incorreto ! "
					txtResposta.setText(" Errado !");
					txtResposta.setTextColor(getResources().getColor(R.color.resposta_incorreta));
					guessButton.setEnabled(false);
					
				}
	
}//Fim da função...




//Função para deshabilitar os botões...
	private void disableButtons(){
	
		for(int row = 0; row < buttonTableLayout.getChildCount(); ++row){
			TableRow tableRow = (TableRow) buttonTableLayout.getChildAt(row);
			for(int i = 0; i < tableRow.getChildCount(); ++i){
				tableRow.getChildAt(i).setEnabled(false);
			} //fim do segundo for
		} // fim do primeiro for
	  } //Fim da função...
}
