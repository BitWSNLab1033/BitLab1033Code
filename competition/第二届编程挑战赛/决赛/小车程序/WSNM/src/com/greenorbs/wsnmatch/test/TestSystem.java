package com.greenorbs.wsnmatch.test;

public class TestSystem {

	public static void main(String[] args) {
		
		// �жϵ�ǰϵͳ�Ƿ�֧��Java AWT Desktop��չ
		if (java.awt.Desktop.isDesktopSupported()) {
			try {
				// ����һ��URIʵ��
				java.net.URI uri = java.net.URI.create("http://www.163.com");
				// ��ȡ��ǰϵͳ������չ
				java.awt.Desktop dp = java.awt.Desktop.getDesktop();
				// �ж�ϵͳ�����Ƿ�֧��Ҫִ�еĹ���
				if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
					// ��ȡϵͳĬ�������������
					dp.browse(uri);
				}
			} catch (java.lang.NullPointerException e) {
				// ��ΪuriΪ��ʱ�׳��쳣
			} catch (java.io.IOException e) {
				// ��Ϊ�޷���ȡϵͳĬ�������
			}
		}
	}
}