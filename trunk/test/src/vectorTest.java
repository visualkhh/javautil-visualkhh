import java.util.Vector;

public class vectorTest
{
	public static void main(String[] args)
	{
		Vector<String> vc =new Vector<String>();

		System.out.println(vc.size()+"  Start size = "+vc.capacity());
		vc.add("�ֱ���");
		vc.add("�㼼��");
		vc.add("�Ǽ���");
		vc.add("�迵ȿ");
		vc.add("������");
		vc.add("�ֱ���");
		vc.add("�㼼��");
		vc.add("�Ǽ���");
		vc.add("�迵ȿ");
		vc.add("������");
		vc.add("������");
		vc.add("�Ǽ���");
		vc.add("�迵ȿ");
		vc.add("������");
		vc.add("�ֱ���");
		vc.add("�㼼��");
		vc.add("�Ǽ���");
		vc.add("�迵ȿ");
		vc.add("������");
		vc.add("������");
		vc.add("�Ǽ���");
		vc.add("�迵ȿ");
		vc.add("������");
		vc.add("�ֱ���");
		vc.add("�㼼��");
		vc.add("�Ǽ���");
		vc.add("�迵ȿ");
		vc.add("������");
		vc.add("������");

		//���� ����
		boolean boo = vc.contains("������");
		System.out.println(boo);
		
		
		//������ �ε����� ��Ҹ� ��ȯ�Ѵ�. �ٸ� Object�� ���±� ������ ����ȯ�� ���־� �Ѵ�.
		String temp = (String)vc.elementAt(2);	
		System.out.println("��� index 2: " + temp);
		
		
		vc.remove(0);			//public Object  remove(int index)		  return ����������Ʈ	 
		vc.removeElementAt(1);	//public void removeElementAt(int index)
		//������ �ε����� ������Ʈ ����. �� �ڸ��� �ڵ������� ���� ������Ʈ�� �̵��Ͽ� ä������.
		System.out.println(vc.size()+"  Remove2 size = "+vc.capacity());
		vc.clear();			//�ش� ������ ��� element(���) ����
		System.out.println(vc.size()+" Clear  size = "+vc.capacity());
		
		System.out.println();
		
		////////Restart//////////////
		
		vc.addElement("���� ��� 1");
		vc.addElement("���� ��� 2");
		
		vc.insertElementAt("���� ��� 3", 1);	//������ �ε����� ��� ����
		
		System.out.println("��� index 0: " + (String)vc.elementAt(0));
		System.out.println("��� index 1: " + (String)vc.elementAt(1));
		System.out.println("��� index 2: " + (String)vc.elementAt(2));
		
		vc.clear();
		
		
		vc.addElement("Vector ��� 0");
		vc.addElement("Vector ��� 1");
		vc.addElement("Vector ��� 2");
		vc.addElement("Vector ��� 3");
		vc.addElement("Vector ��� 4");
		vc.addElement("Vector ��� 5");
		
		for (int x = 0; x < 10; x++)
		{
			for (int i = 0; i < 6; i++)
			{
				System.out.println(vc.remove(0));
				vc.addElement("VectorPPP ��� "+i);  
			}
				
		}
		
		
		System.out.println(vc.size()+"   size = "+vc.capacity());
		
		
		Vector<String> tt =new Vector<String>();
		tt.add("zz");
		System.out.println(tt.remove(0));
		System.out.println(tt.remove(0));
		
		
		
	}
}
