/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aula9;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author Guilherme
 */
public class Aula9 {

    
    public static void main(String[] args) {

        //HashMap<Integer, HashMap<Character, ArrayList<Integer>>> nfa = new HashMap<>();
        
         HashMap<Integer, HashMap<Character, Integer>> dfa = new HashMap<>();
         

         //TESTE DO EXERCICIO AUTOMATOS LAB FEITO EM SALA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         HashMap<Character, Integer> transitions = new HashMap<>();

    
        transitions.put('0',1);
        transitions.put('1',2);

        dfa.put(0,transitions);
        //-------------------------------------------
        transitions = new HashMap<>();
        
        transitions.put('0', 0);
        transitions.put('1', 3);
        
        dfa.put(1,transitions);
        //----------------------------------------------
        transitions = new HashMap<>();
        
        transitions.put('0', 4);
        transitions.put('1', 5);
        
        dfa.put(2,transitions);
        //----------------------------------------------
        
        transitions = new HashMap<>();
        
        transitions.put('0', 2);
        transitions.put('1', 5);
        
        dfa.put(3,transitions);
        //----------------------------------------------
        transitions = new HashMap<>();
        
        transitions.put('0', 3);
        transitions.put('1', 5);
        
        dfa.put(4,transitions);
        //----------------------------------------------
        transitions = new HashMap<>();
        
        transitions.put('0', 5);
        transitions.put('1', 5);
        
        dfa.put(5,transitions);
        //----------------------------------------------
        
        
        HashSet<Character> alfabeto = new HashSet();
        alfabeto.add('0');
        alfabeto.add('1');

        HashSet<Integer> finais = new HashSet<>();
        finais.add(2);
        finais.add(3);
        finais.add(4);
         
         
         
       //TESTE DE OUTRO CENARIO (CENARIO AUTOMATOS TEORIA SLIDE 5)!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//         HashMap<Character, Integer> transitions = new HashMap<>();
//
//       
//        transitions.put('a', 2);
//        transitions.put('b', 1);
//
//        dfa.put(0,transitions);
//        //-------------------------------------------
//        transitions = new HashMap<>();
//        
//        transitions.put('a', 1);
//        transitions.put('b', 0);
//        
//        dfa.put(1,transitions);
//        //----------------------------------------------
//        transitions = new HashMap<>();
//        
//        transitions.put('a', 4);
//        transitions.put('b', 5);
//        
//        dfa.put(2,transitions);
//        //----------------------------------------------
//        
//        transitions = new HashMap<>();
//        
//        transitions.put('a', 5);
//        transitions.put('b', 4);
//        
//        dfa.put(3,transitions);
//        //----------------------------------------------
//        transitions = new HashMap<>();
//        
//        transitions.put('a', 3);
//        transitions.put('b', 2);
//        
//        dfa.put(4,transitions);
//        //----------------------------------------------
//        transitions = new HashMap<>();
//        
//        transitions.put('a', 2);
//        transitions.put('b', 3);
//        
//        dfa.put(5,transitions);
//        //----------------------------------------------
//        
//        
//        HashSet<Character> alfabeto = new HashSet();
//        alfabeto.add('a');
//        alfabeto.add('b');
//
//        HashSet<Integer> finais = new HashSet<>();
//        finais.add(0);
//        finais.add(4);
//        finais.add(5);

        
        

        System.out.println("AFD: " + dfa);
        System.out.println("Nos finais AFD: " + finais);
        System.out.println("Minimizado: " + minimizaDFA(alfabeto, finais, dfa));
        System.out.println("Nos finais minimizado: " + finais);

    }

    public static HashMap<Integer, HashMap<Character, Integer>> minimizaDFA(HashSet<Character> alfabeto, HashSet<Integer> finais, HashMap<Integer, HashMap<Character, Integer>> dfa) {
        HashMap<Integer, HashSet<Integer>> matriz = encontraNaoEquivalentes(finais, dfa);
        matriz = marcaEstados(matriz, alfabeto, dfa);
        dfa = combinaEstados(matriz, alfabeto, finais, dfa);
        //dfa = formataDFA(alfabeto,dfa);

        return dfa;
    }

    private static HashMap<Integer, HashMap<Character, Integer>> combinaEstados(HashMap<Integer, HashSet<Integer>> matriz, HashSet<Character> alfabeto, HashSet<Integer> finais, HashMap<Integer, HashMap<Character, Integer>> dfa) {
        HashMap<Integer, HashMap<Character, Integer>> dfaFinal = (HashMap<Integer, HashMap<Character, Integer>>) dfa.clone();//new HashMap<>();
        HashMap<Integer, HashSet<Integer>> matriz2 = copy(matriz);

        HashMap<Character, Integer> aux = new HashMap<>();
        HashSet<Integer> chaves = new HashSet<>();

        for (int i = 0; i < matriz.size(); i++) {
            Object[] lista = matriz.get(i).toArray();
            if (lista.length > 0) {
                for (int j = 0; j < lista.length; j++) {
                    chaves.add(i);
                    dfaFinal.remove(lista[j]);
                    matriz.get(lista[j]).remove(i);
                   //matriz.get(i).remove(lista[j]);
                }
            }
        }

        for (int i = 0; i < chaves.size(); i++) {
            aux = dfa.get(chaves.toArray()[i]);

            HashMap<Character, Integer> p = new HashMap<>();
            for (char c : alfabeto) {

                if (matriz2.get(chaves.toArray()[i]).contains(aux.get(c))) {
                    p.put(c, (int) chaves.toArray()[i]);
                }
//                else{ //if (matriz2.get(aux.get(c)).contains(chaves.toArray()[i])) {
//                    p.put(c, (int) chaves.toArray()[i]);
//                } 
                else {
                    if(dfaFinal.get(aux.get(c))!=null)
                    {
                        p.put(c, aux.get(c));
                    }
                    else
                    {
                        for(int z : matriz2.get(aux.get(c)) )
                        {
                            if(chaves.contains(z))
                                p.put(c, z);
                        }
                        //System.out.println();
                    }
                } 
            }
            dfaFinal.put((int) chaves.toArray()[i], p);//NAO ESTA TRATANDO TODOS OS CASOS
        }
        
        for (int i = 0; i < matriz2.size(); i++) {
            Object[] lista = matriz2.get(i).toArray();
            if (lista.length > 0) {
                for (int j = 0; j < lista.length; j++) {
                    chaves.add(i);
                    dfaFinal.remove(lista[j]);
                    matriz2.get(lista[j]).remove(i);
                }
            }
        }

        chaves = (HashSet<Integer>) finais.clone();
        for (int x : chaves) {
            if (!dfaFinal.containsKey(x)) {
                finais.remove(x);
            }
        }

        return dfaFinal;
    }

    public static HashMap<Integer, HashSet<Integer>> copy(HashMap<Integer, HashSet<Integer>> original) {
        HashMap<Integer, HashSet<Integer>> copy = new HashMap<Integer, HashSet<Integer>>();
        for (Map.Entry<Integer, HashSet<Integer>> entry : original.entrySet()) {
            copy.put(entry.getKey(), new HashSet<Integer>(entry.getValue()));
        }
        return copy;
    }

    public static HashMap<Integer, HashSet<Integer>> marcaEstados(HashMap<Integer, HashSet<Integer>> matriz, HashSet<Character> alfabeto, HashMap<Integer, HashMap<Character, Integer>> dfa) {
        ArrayList<Integer> lista;
        Object[] destinos;
        boolean alteracao;
        do {
            alteracao = false;
            for (int i = 0; i < matriz.size(); i++) {
                destinos = matriz.get(i).toArray();
                for (int j = 0; j < destinos.length; j++) {

                    lista = verificaCombinacao(i, (int) destinos[j], alfabeto, matriz, dfa);

                    for (int k = 0; k < lista.size(); k += 2) {
                        if (!(matriz.get(lista.get(k)).contains(lista.get(k + 1))) && lista.get(k) != lista.get(k + 1)) {
                            matriz.get(i).remove((int) destinos[j]);
                            matriz.get((int) destinos[j]).remove(i);
                            alteracao = true;
                        }
                    }
                }
            }
        } while (alteracao);
        return matriz;
    }

    private static ArrayList<Integer> verificaCombinacao(int i, int j, HashSet<Character> alfabeto, HashMap<Integer, HashSet<Integer>> matriz, HashMap<Integer, HashMap<Character, Integer>> dfa) {
        ArrayList<Integer> lista = new ArrayList();

        for (char c : alfabeto) {
            lista.add(dfa.get(i).get(c));
            lista.add(dfa.get(j).get(c));
        }
        return lista;
    }

    public static HashMap<Integer, HashSet<Integer>> encontraNaoEquivalentes(HashSet<Integer> finais, HashMap<Integer, HashMap<Character, Integer>> dfa) {
        HashMap<Integer, HashSet<Integer>> matriz = new HashMap();
        HashSet<Integer> aux;
        for (int i = 0; i < dfa.size(); i++) {
            aux = new HashSet();
            for (int j = 0; j < dfa.size(); j++) {
                if (i != j) {
                    if (ehFinal(i, finais)) {
                        if (ehFinal(j, finais)) {
                            aux.add(j);
                        }
                    }

                    if (!ehFinal(i, finais)) {
                        if (!ehFinal(j, finais)) {
                            aux.add(j);
                        }
                    }
                }
            }
            matriz.put(i, aux);
        }
        return matriz;
    }

    public static boolean ehFinal(int state, HashSet<Integer> finais) {

        for (int f : finais) {
            if (state == f) {
                return true;
            }
        }

        return false;
    }
    
    public static boolean runDfa(HashMap<Integer, HashMap<Character, Integer>> dfa, String input,
            List<Integer> finalStates) {

        int state = 0;

        for (char c : input.toCharArray()) {
            try {
                HashMap<Character, Integer> transitions = dfa.get(state);
                state = transitions.get(c);
            } catch (Exception e) {
                return false;
            }
        }

        return finalStates.contains(state);
    }

}
