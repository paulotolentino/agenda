package com.techboxsys.agendacontato.back4app;

import android.util.Log;

import java.util.Date;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.SaveCallback;
import com.techboxsys.agendacontato.modelo.Contatos;

public abstract class B4AContato {

    public static void createContact(Contatos contato) {
        ParseObject entity = new ParseObject("contact");

        entity.put("idLocal", contato.getId());
        entity.put("nome", contato.getNome());
        entity.put("apelido", contato.getApelido());
        entity.put("numero", contato.getNumero());

        // Saves the new object.
        // Notice that the SaveCallback is totally optional!
        entity.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.i("ParseException", "Erro: ", e);
            }
        });
    }

    public static void updateContact(Contatos contato) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("contact");
        query.whereEqualTo("idLocal", contato.getId());
        Log.i("a", "updateContact: " + contato.getId());
        final String nome = contato.getNome();
        final String numero = contato.getNumero();
        final String apelido = contato.getApelido();

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject contact, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    contact.put("nome", nome);
                    contact.put("apelido", apelido);
                    contact.put("numero", numero);
                    contact.saveInBackground();
                } else {
                    // Failed
                    Log.i("ParseException", "Erro: ", e);
                }
            }
        });
    }

    public static void deleteContact(Contatos contato) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("contact");
        query.whereEqualTo("idLocal", contato.getId());

        query.findInBackground(new FindCallback<ParseObject>() {
           @Override
           public void done(final List<ParseObject> contact, ParseException e) {
               if (e == null) {
                   try{
                       contact.get(0).deleteInBackground(new DeleteCallback() {
                           @Override
                           public void done(ParseException e) {
                               if (e == null) {
                                   // Success
                               } else {
                                   // Failed
                                   Log.i("ParseException", "Erro: ", e);
                               }
                           }
                       });
                   }catch (IndexOutOfBoundsException err){
                       // Failed
                       Log.i("ParseException", "IndexOutOfBoundsException: ", err);

                   }

               } else {
                   // Something is wrong
               }
           };
       });
    }

    public static void deleteContact() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("contact");
        //query.whereEqualTo("idLocal", 1);

        // Retrieve the object by id
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> contact, ParseException e) {
                if (e == null) {
                    try{
                        contact.get(0).deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    // Success
                                } else {
                                    // Failed
                                    Log.i("ParseException", "Erro: ", e);
                                }
                            }
                        });
                    }catch (IndexOutOfBoundsException err){
                        // Failed
                        Log.i("ParseException", "IndexOutOfBoundsException: ", err);

                    }
                } else {
                    // Something is wrong
                }
            };
        });
    }
}
