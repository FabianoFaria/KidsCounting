package com.example.kidscounting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class NumeroDecrecente extends NumerosCrescente{
	
	private OnClickListener guessButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v)
		{
			submitGuess((Button) v);
		}
	};
	
	private TableLayout buttonTable;
	private List<String> numeros; //Resposta do exercicio...
	private List<String> respostaUser;//Resposta do ususario...
	private TextView linhaContagem;
	private Handler handler;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_numeros_crescente);
	
		TextView tituloTela = (TextView)findViewById(R.id.TituloTela);
		tituloTela.setText("Digite os numeros de 10 à 1"); //Setta o texto do titulo da tela...
		
		//Mapeando os objetos que seram utilizados no App...
		buttonTable = (TableLayout)findViewById(R.id.TableBtnLayout); //tabela com os botões...
		numeros = new ArrayList<String>();
		respostaUser = new ArrayList<String>();
		linhaContagem = (TextView)findViewById(R.id.TextContagem); // TextView para visualizar a contagem inserida pelo ususario...
		handler = new Handler();
		iniciaContagem();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.numeros_crescente, menu);
		return true;
	}
	
	//Inicia o processo de contagem...
		private void iniciaContagem(){
			
			linhaContagem.setText("10-"); //Apaga o texto para inicia a contagem...
			numeros.clear();	//Prepara a lista para os valores dos numeros para este exercicio...
			respostaUser.clear(); //Limpa as resposta dadas pelo usuario...
			
			
			verificaContage();
			
		}
		
		private void verificaContage(){
			
			 
			for(int i = 0; i < 9; i++ ){ // Criar um Array para a contagem...
				
				numeros.add(String.valueOf(i));
			}
			
			// Limpar os Botôes de TableRow
			for (int row = 0; row < buttonTable.getChildCount(); ++row){
				 ((TableRow) buttonTable.getChildAt(row)).removeAllViews();
			}
			
			Collections.shuffle(numeros); //embaralha a lista...
			/*
			for(int i = 0; i < numeros.size(); i ++){
				System.out.println(numeros.get(i));
			}*/
				
			//Obter uma referencia para o serviço LayoutInflater...
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			//Adiciona os componentes Buttom de resposta, de acordo com o este exercicio...
			
			for ( int row = 0; row < 3; row++){ //O 3 aqui se deve ao fato de que são 3 linhas....
				TableRow currentTableRow = getTableRow(row);
				
				for(int column = 0; column < 3; column++){
					//Infla guess_button.xml para criar novo botões-
					Button newGuessButton = (Button) inflater.inflate(R.layout.guess_button, null);
					
					//obtem o valor numerico da lista e e configura como texto do newguessButton
					String numeroL = numeros.get((row * 3) + column); //(row * 3) + column
					System.out.println("Numero impresso" + numeroL);
					newGuessButton.setText(getNomeNumero(numeroL));
					
					System.out.println(numeroL);
					//registra answerbuttonListener para responder aos cliques dos botões...
					newGuessButton.setOnClickListener(guessButtonListener);
					currentTableRow.addView(newGuessButton);
				}
			} //Fim do primeiro for
			
		} //Fim do metodo 
		
		private TableRow getTableRow(int row) {	
			return (TableRow) buttonTable.getChildAt(row);
		}
		
		private String getNomeNumero(String name) { //metodo para corrigir o texto dos botões...
			
			int inteiro = Integer.parseInt(name);
			
			return name = String.valueOf(inteiro + 1);
		}

		public void retorno(View view){
			finish();
		}
	
	
		
		//Altera a Textview do resultado
		private void alteraTextView(int i) {
			String tempString = ""; 
		    //tempString = linhaContagem.getText().toString() + "-" + i;
			for(int b = 9 ; b >= i ; b--){
				tempString = tempString + (String.valueOf(b) + "-");
				}
		    	linhaContagem.setText("10-" + tempString );
			}
	
	
	
	private void submitGuess(Button guessButton) {
		
		
		String guess = guessButton.getText().toString();
		
	    respostaUser.add(guess);
	    int indice = respostaUser.size();
	    int y = 9;
	    
	    for(int i = 1; i < respostaUser.size(); i++){
	    	
	    	//final int LastIndex = i;
	    	
	    	if(respostaUser.get(i).contains(String.valueOf(y))){ //está digitando o numero na sequencia correta...
	    		//Metodo para alterar o resultado do Textview
	    		alteraTextView(y);
	    		guessButton.setEnabled(false);
	    		y--;
	    	}	// Fim do if...
	    	else{ // Usuario digitou um numero que não responde a sequencia..
	    		final String tempString = linhaContagem.getText().toString();
	    		linhaContagem.setText(" ");
	    		linhaContagem.setText(guess + " não é o numero certo, tente outro numero...");
	    		
	    		handler.postDelayed(
	    				new Runnable()
	    				{
	    					@Override
	    					public void run()
	    					{	
	    						linhaContagem.setText(" ");
	    						linhaContagem.setText(tempString);
	    						
	    					}
	    				}, 2000); //settar um atraso de 2 segundos para ler a mensagem
	    		respostaUser.remove(indice - 1);
	    		guessButton.setEnabled(true);
	    		}//Fim do else...
	    }//Fim do for...
	    
	    //Aqui faz a verificação do arry list e terminar o exercicio...
	    if(respostaUser.size() == 10){
	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	
	    	builder.setTitle("Você completou a sequencia corretamente!");
	    	
	    	builder.setCancelable(false);
	    	
	    	//Adicionar o botâo "Resetar a sequencia..."
	    	builder.setPositiveButton("Começar de novo",
	    			new DialogInterface.OnClickListener() 
	    			{
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							iniciaContagem();
						}
					});
	    	//Cria AlertDialog a partir do Builder...
	    	AlertDialog resetDialog = builder.create();
	    	resetDialog.show();
	    }
	}
	
}
