package com.todo.modellist;

import com.todo.common.Utils;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class BaseList<T extends RealmObject> {

    long TimeStampForNewDataLoad = 1000;//1 * 60 * 60 *
    Class<T> m_type = null;
    protected ArrayList<T> m_modelList = null;

    protected BaseList(Class<T> type) {
        m_type = type;
    }

    public void ClearDB() {
        try {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            RealmQuery<T> query = realm.where(m_type);
            RealmResults<T> results = query.findAll();
            results.deleteAllFromRealm();
            realm.commitTransaction();
            realm.close();
        } catch (Exception e) {
            Utils.LogException(e);
        }
    }


    public void loadFromDB() {
        try {
            Realm realm = Realm.getDefaultInstance();
            RealmQuery<T> query = realm.where(m_type);
            RealmResults<T> results = query.findAll();
            m_modelList = new ArrayList<>(results);
        } catch (Exception e) {
            Utils.LogException(e);
        }
    }

    public void ClearDBById(String id, String value) {
        try {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            RealmQuery<T> query = realm.where(m_type).equalTo(id, value);
            RealmResults<T> results = query.findAll();
            results.clear();
            realm.commitTransaction();
            realm.close();
        } catch (Exception e) {
            Utils.LogException(e);
        }
    }

    public void loadFromDBById(String id, String value) {
        try {
            Realm realm = Realm.getDefaultInstance();
            RealmQuery<T> query = realm.where(m_type).equalTo(id, value);
            RealmResults<T> results = query.findAll();
            m_modelList = new ArrayList<>(results);
        } catch (Exception e) {
            Utils.LogException(e);
        }
    }

    public ArrayList<T> getList() {
        if (m_modelList == null)
            m_modelList = new ArrayList<T>();
        return m_modelList;
    }


}
