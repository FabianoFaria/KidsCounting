package com.example.kidscounting;

/*
 * Fabiano Faria Hatori
 */
import java.util.Random;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class Aritimetica extends Activity {
	
	private OnClickListener guessButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v)
		{
			submitGuess((Button) v);
		}
	};
	
	private TextView contadorQuestao;
	private TextView questao;
	private TextView txtResposta;
	private TableLayout buttonTableLayout;
	private Button btnSair;
	
	private Handler handler;
	
	private int totalTentativas; // Total de tentativas...
	private int respostaCorreta; //resposta de cada questão
	private int totalRespostasCorretas; //Total de resposta certas...
	private int rowNunbers;
	
	private Random random;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aritimetica);
		
		contadorQuestao = (TextView)findViewById(R.id.contadorQuestao);
		questao = (TextView)findViewById(R.id.sentencaAritimetica);
		buttonTableLayout =(TableLayout)findViewById(R.id.TableBtnLayoutAritimetica);
		txtResposta = (TextView)findViewById(R.id.textResposta);
		btnSair = (Button) findViewById(R.id.btnSair);
		rowNunbers = 1;
		random = new Random();
		handler = new Handler();
		
		resetPergunta();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.aritimetica, menu);
		return true;
	}
	
	public void retorno(View view){
		finish();
	}
	
	
	private void resetPergunta(){
		
		
		totalTentativas = 0;
		totalRespostasCorretas = 0;
		txtResposta.setText(" ");
		contadorQuestao.setText(" ");
		carregaPergunta();
	}
	
	
	private void carregaPergunta(){
		
		//Codigo para gerar a pergunta...
		int primeiroNumero = random.nextInt(10);
		int segundoNumero = random.nextInt(10);
		int operacao = random.nextInt(2);
		String stringOperacao = "";
		String operacaoString = "";
		txtResposta.setText(" ");
	    respostaCorreta = 0;
		switch(operacao){
			case 1: 
				stringOperacao = "-";
				respostaCorreta = primeiroNumero - segundoNumero;
				break;
			default: 
				stringOperacao = "+";
				respostaCorreta = primeiroNumero + segundoNumero;
				break;
		}
		
		operacaoString = primeiroNumero + " " + stringOperacao + " " + segundoNumero;
		System.out.println(respostaCorreta +" "+ operacaoString + " " + operacao);	
		
		questao.setText(operacaoString);
		//Codigo para Inflar o layout...
		
			//Limpar os botões existentes
		
			for(int row = 0; row < buttonTableLayout.getChildCount(); ++row)
				((TableRow) buttonTableLayout.getChildAt(row)).removeAllViews();
			
			//Obter a referencia para o LayoutInflater...
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			for (int row = 0; row < rowNunbers; row++ ){
				
				TableRow currentTableRow = getTableRow(row);
				
				// coloca os componentes Buttom em currentTableRow
				for(int column = 0; column < 3; column++){
					//infla o guessguess_buttom.xml...
					Button newguessButtom = (Button) inflater.inflate(R.layout.guess_button, null);
					
					//Settar o texto dos botôes...
					int novoNumero = random.nextInt(10);
					if (novoNumero == respostaCorreta){
						novoNumero = novoNumero++;
					}
					String novoNumeroTxt = String.valueOf(novoNumero);
					newguessButtom.setText(novoNumeroTxt);
					
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
	
	//Processa a resposta do usuario...
	private void submitGuess( Button guessButton){
		
		String guess = guessButton.getText().toString();
		String respostaQuestão = String.valueOf(respostaCorreta);
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
			} // fim do else
		} //fim do if
		
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
